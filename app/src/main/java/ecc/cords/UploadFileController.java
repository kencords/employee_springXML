package ecc.cords;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileController {

	public EmployeeDTO parseFile(MultipartFile file) throws Exception;
}