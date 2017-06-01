package ecc.cords;

import org.aspectj.lang.JoinPoint;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

public class LoggerService {

	private static final Logger logger = LoggerFactory.getLogger(LoggerService.class);

	public void beforeSaving(JoinPoint joinPoint) {
		logger.info("Saving record: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	public void beforeUpdating(JoinPoint joinPoint) {
		logger.info("Updating record: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	public void beforeDeleting(JoinPoint joinPoint) {
		logger.info("Deleting record: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	public void beforeGettingRecord(JoinPoint joinPoint) {
		logger.info("Getting single record: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	public void beforeGettingRecords(JoinPoint joinPoint) {
		logger.info("Getting multiple records: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	public void beforeOnSubmit(JoinPoint joinPoint) {
		logger.info("Called onSubmit()");
	}

	public void beforeShowForm(JoinPoint joinPoint) {
		logger.info("Called showForm()");
	}

	public void beforeParsing(JoinPoint joinPoint) {
		logger.info("Parsed file: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	private String getArgValues(JoinPoint joinPoint) {
		StringBuilder sb = new StringBuilder();
		Object[] args = joinPoint.getArgs();
		for(int i=0;i<args.length;i++) {
			String val = (args[i] instanceof MultipartFile? ((MultipartFile) args[i]).getOriginalFilename() : args[i] + "");
			sb.append(val + (i<args.length-1? ", " : ""));
		}
		return sb.toString();
	}
}
