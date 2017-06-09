package ecc.cords.impl;

import org.aspectj.lang.JoinPoint;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

import ecc.cords.LoggerService;

public class LoggerServiceImpl implements LoggerService {

	private static final Logger logger = LoggerFactory.getLogger(LoggerServiceImpl.class);

	/*@Override
	public void beforeOnSubmit(JoinPoint joinPoint) {
		logger.info("Called onSubmit()");
	}

	@Override
	public void beforeShowForm(JoinPoint joinPoint) {
		logger.info("Called showForm()");
	}

	@Override
	public void beforeShowPage(JoinPoint joinPoint) {
		logger.info("Called " + joinPoint.getSignature().toShortString());
	}

	@Override
	public void beforeParsing(JoinPoint joinPoint) {
		logger.info("Parsed file: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}*/

	@Override
	public void beforeSaving(JoinPoint joinPoint) {
		logger.info("Saving record: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	@Override
	public void beforeUpdating(JoinPoint joinPoint) {
		logger.info("Updating record: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	@Override
	public void beforeDeleting(JoinPoint joinPoint) {
		logger.info("Deleting record: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	@Override
	public void beforeGettingRecord(JoinPoint joinPoint) {
		logger.info("Getting single record: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
	}

	@Override
	public void beforeGettingRecords(JoinPoint joinPoint) {
		logger.info("Getting multiple records: " + joinPoint.getSignature().toShortString() + " with args " + getArgValues(joinPoint));
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
