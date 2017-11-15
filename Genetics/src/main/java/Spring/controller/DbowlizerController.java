package Spring.controller;

import Spring.assets.FileFilter;
import Spring.assets.JsonResponse;
import Spring.entity.DbSettings;
import Spring.entity.Ontology;
import Spring.model.OntologyRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class DbowlizerController {

	@Autowired
	OntologyRepository ontologyRepository;

	@RequestMapping(value = "/dbowlizer", method = RequestMethod.GET)
	public String dbowlizer(Model model) {
		return "dbowlizer";
	}

	@RequestMapping(value = "/dbowlizer/listOntologies", method = RequestMethod.GET)
	public String listOntologies(Model model) {
		ontologyRepository.deleteAll();
		ontologyRepository.save(getStoredOntologies());
		model.addAttribute("ontologies", ontologyRepository.findAll());
		return "dbowlizerOutputs";
	}

	@RequestMapping(value = "/dbowlizer/run", method = RequestMethod.POST)
	@ResponseBody
	public JsonResponse runDbowlizer(Model model, @Validated @ModelAttribute("dbSettings") DbSettings dbSettings, BindingResult result) {
		JsonResponse response = new JsonResponse();
		if (result.hasErrors()) { //Check if form has validation errors
			response.setStatus("fail");
			response.setErrorMessageList(getErrorMessages(result));
		} else {
			try {
				System.out.println("Starting backend");
//				startDbowlizerBackend(dbSettings);
				response.setStatus("success");
			} catch (Exception e) {
				System.out.println(e);
				response.setStatus("fail");
				List<String> errorMessageList = new ArrayList<>();
				errorMessageList.add("Something went wrong while executing demo, check the configuration file");
				response.setErrorMessageList(errorMessageList);
			}
		}
		return response;
	}

	private List<String> getErrorMessages(BindingResult result) {
		List<String> errorMessageList = new ArrayList<>();
		for (FieldError fieldError : result.getFieldErrors()) {
			errorMessageList.add(fieldError.getDefaultMessage());
		}
		return errorMessageList;
	}

	private List<Ontology> getStoredOntologies() {
//		String ontologiesDir = System.getProperty("user.home") + "Downloads/DbowlizerOutputs/";
		String ontologiesDir = System.getProperty("java.io.tmpdir") + "/DbowlizerOutputs/";

		FileFilter fileFilter = new FileFilter();
		List<Ontology> ontologies = new LinkedList<>();

		for (String directory : fileFilter.getDirectories(ontologiesDir)) {
			String databaseName = fileFilter.getOwlFiles(ontologiesDir + directory)[0].getName().split("-")[0];
			String extractedOntologyPath = fileFilter.getOwlFiles(ontologiesDir + directory)[0].getAbsolutePath();
			String mappingsDirectory = ontologiesDir + directory + "/mappings/";
			String mappingsOntologyPath = mappingsDirectory + "relational-to-ontology-mapping-output.owl";
			Date creationDate = null;
			SimpleDateFormat sdfmt1 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			try {
				creationDate = sdfmt1.parse(directory);
			} catch (ParseException ignored) {
			}

			ontologies.add(new Ontology(databaseName, extractedOntologyPath, mappingsOntologyPath, mappingsDirectory, creationDate));
		}
		return ontologies;
	}

//	private void startDbowlizerBackend(DbSettings dbSettings) {
//		String outputDir = System.getProperty("java.io.tmpdir") + "/DbowlizerOutputs/";
//		outputDir = outputDir.replace("\\", "/");
//		System.out.println(outputDir);
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//		String date = dateFormat.format(new Date());
//
//		boolean directoryCreated = new File(outputDir + date).mkdir();
//
//		if (!directoryCreated) return;
//
//
//		//Settings
//		Settings settings = new Settings("/schema2owl.config.properties");
//		settings.setOutputDir(settings.getOutputDir() + date + "/mappings/");
//
//		settings.setOntologyFile(settings.getOutputDir() + "relational-to-ontology-mapping-output.owl");
//		settings.setOutputDirFile(settings.getOutputDirFile() + date + "/");
//
//// TODO: Fix directories that may have spaces
////		settings.setOutputDir(settings.getOutputDir().replace(" ", "~"));
////		settings.setOntologyFile(settings.getOntologyFile().replace(" ", "~"));
////		settings.setOutputDirFile(settings.getOutputDirFile().replace(" ", "~"));
//
//
//		System.out.println("\nThis is from the controller");
//
//		System.out.println("Output Dir: " + settings.getOutputDir());
//		System.out.println("Output Ontology File: " + settings.getOntologyFile());
//		System.out.println("Output Dir File: " + settings.getOutputDirFile());
//
//		//Input from html
//		settings.setDriver(dbSettings.getDriver());
//		settings.setHost(dbSettings.getHost());
//		settings.setPort(dbSettings.getPort());
//		settings.setDbname(dbSettings.getDbName());
//		settings.setUser(dbSettings.getUsername());
//		settings.setPassword(dbSettings.getPassword());
//		if (dbSettings.getSchema() != null)
//			settings.setRealSchema(dbSettings.getSchema());
//
//
//		//Call to dbowlizer with the settings as input
//		Service dbowlizer = new Service();
//		dbowlizer.service(settings);
//	}

	@RequestMapping(value="/downloadZipFile/{id}", produces="application/zip")
	public void zipFiles(HttpServletResponse response, @PathVariable("id")int id) throws IOException {

		//setting headers
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");

		ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());


		String path = System.getProperty("java.io.tmpdir") + "/DbowlizerOutputs/";
		File directory = new File(path + id);
		File[] files = directory.listFiles();

		//packing files
		for (File file : files) {
			//new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
			zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
			FileInputStream fileInputStream = new FileInputStream(file);

			IOUtils.copy(fileInputStream, zipOutputStream);

			fileInputStream.close();
			zipOutputStream.closeEntry();
		}

		zipOutputStream.close();
	}
}