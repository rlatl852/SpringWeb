package com.SpringWeb.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardAttachDTO {

	private String uuid;
	private String uploadPath;
	private String fileName;
	private int bno;
	private boolean fileType;
}
