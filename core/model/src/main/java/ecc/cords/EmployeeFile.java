package ecc.cords;

import org.springframework.web.multipart.MultipartFile;

public class EmployeeFile {

	private MultipartFile file;

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public MultipartFile getFile() {
		return file;
	}
}