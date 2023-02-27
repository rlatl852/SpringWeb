package com.SpringWeb.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.SpringWeb.domain.BoardAttachDTO;
import com.SpringWeb.mapper.BoardAttachMapper;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class FileCheckTask {

	@Autowired
	private BoardAttachMapper attachMapper;
	
	private String getFolderYesterday() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			String str = sdf.format(cal.getTime());
		return null;
	}
	
	//@Scheduled(cron = "* 0 * * * *")
	//@Scheduled(cron = "0,5,10,15,20,25,30,35,40,45,50,55 0 * * * *") 초,분,시,일,월,년
	public void checkFiles() {
		log.warn("File Check Task run ....." + new Date());
		log.warn("==============================");
		
		// file list in database
		List<BoardAttachDTO> fileList = attachMapper.getOldFiles();
		
		// read for check file in directory with database file list
		List<Path> fileListPaths = fileList.stream().map(vo -> Paths.get("C:\\Users\\slumb\\Desktop\\김영환\\uploadFile\\", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName())).collect(Collectors.toList());
		
		//image file has thumnail file
		fileList.stream().filter(vo -> vo.isFileType() ==true).map(vo -> Paths.get("C:\\Users\\slumb\\Desktop\\김영환\\uploadFile\\", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName())).forEach(p -> fileListPaths.add(p));
		
		log.warn("==============================");
		
		fileListPaths.forEach(p -> log.warn(p));
		
		//files in yesterday directory
		File targetDir = Paths.get("C:\\Users\\slumb\\Desktop\\김영환\\uploadFile\\", getFolderYesterday()).toFile();
		
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);
		
		log.warn("------------------------------");
		
		for(File file : removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete();
		}
	}
}
