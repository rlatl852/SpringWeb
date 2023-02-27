package com.SpringWeb.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.SpringWeb.domain.BoardAttachDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {

	@GetMapping("/uploadForm")
	public String uploadForm() {
		return "board/uploadForm";
	}

	@GetMapping("/uploadAjax")
	public String uploadAjax() {
		return "board/uploadAjax";
	}

	@PostMapping("/uploadFormAction")
	// <input type="file" name="uploadFile" multiple>의 name속성으로 변수를 지정 처리
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		String uploadFolder = "C:\\Users\\slumb\\Desktop\\김영환\\uploadFile";
		for (MultipartFile multipartFile : uploadFile) {
			// log.info("-------------------------------");
			// log.info("" + multipartFile.getOriginalFilename());
			// log.info("" + multipartFile.getSize());

			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				e.printStackTrace();
			} // end catch
		} // end for
	}

	@PostMapping("/uploadAjaxAction")
	@ResponseBody
	public ResponseEntity<List<BoardAttachDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		List<BoardAttachDTO> list = new ArrayList<BoardAttachDTO>();

		String uploadFolder = "C:\\Users\\slumb\\Desktop\\김영환\\uploadFile";
		String uploadFolderPath = getFolder();

		// make folder
		File uploadPath = new File(uploadFolder, getFolder());
		System.out.println("upload path : " + uploadFile);

		// make yyyy/MM/dd folder
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}

		for (MultipartFile multipartFile : uploadFile) {
			BoardAttachDTO attachVO = new BoardAttachDTO();

			String uploadFileName = multipartFile.getOriginalFilename();
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			System.out.println("only file name : " + uploadFileName);
			attachVO.setFileName(uploadFileName);

			// 첨부파일은 randomUUID()를 이용해 임의의 값을 생성하여 이름에 붙여 중복을 피하도록 함
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;

			try {
				// File saveFile = new File(uploadFolder, uploadFileName);
				File saveFile = new File(uploadPath, uploadFileName);

				multipartFile.transferTo(saveFile);

				attachVO.setUuid(uuid.toString());

				attachVO.setUploadPath(uploadFolderPath);

				// check image type file
				if (checkImageType(saveFile)) {
					attachVO.setFileType(true);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
				}
				list.add(attachVO);
			} catch (Exception e) {
				e.printStackTrace();
			} // end catch
		} // end for
		return new ResponseEntity<List<BoardAttachDTO>>(list, HttpStatus.OK);
	}

	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {
		System.out.println("fileName : " + fileName);
		File file = new File("C:\\Users\\slumb\\Desktop\\김영환\\uploadFile\\" + fileName);
		System.out.println("file : " + file);
		ResponseEntity<byte[]> result = null;
		
		try {
			org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
			headers.add("ContentType", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type){
		File file;
		try {
			file = new File("C:\\Users\\slumb\\Desktop\\김영환\\uploadFile\\" + URLDecoder.decode(fileName, "UTF-8"));
			file.delete();
			if(type.equals("image")) {	// 이미지 파일일 경우 원본 파일도 삭제
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				file = new File(largeFileName);
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(String fileName){
			Resource resource = new FileSystemResource("C:\\Users\\slumb\\Desktop\\김영환\\uploadFile\\" + fileName);
			if(resource.exists() == false) { // exists()는 지정한 경로에 파일이 존재하는 지 확인
				return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
			}
			String resourceName = resource.getFilename();
			
			String resourceOriginalName = resourceName.substring(resourceName.indexOf("_")+1);
			
			HttpHeaders headers = new HttpHeaders();
			
			try {
				String downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
				headers.add("Content-Disposition", "attachment; filename=" + new String(downloadName.getBytes("UTF-8"), "ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}

	private boolean checkImageType(File file) { // 업로드 되는 파일이 이미지 파일인지 검사하는 메서드
		try {
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
