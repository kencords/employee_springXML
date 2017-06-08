package ecc.cords;

import org.aspectj.lang.JoinPoint;

public interface LoggerService {

	public void beforeSaving(JoinPoint joinPoint);

	public void beforeUpdating(JoinPoint joinPoint);

	public void beforeDeleting(JoinPoint joinPoint);

	public void beforeGettingRecord(JoinPoint joinPoint);

	public void beforeGettingRecords(JoinPoint joinPoint);

	public void beforeOnSubmit(JoinPoint joinPoint);

	public void beforeShowForm(JoinPoint joinPoint);

	public void beforeParsing(JoinPoint joinPoint);
}