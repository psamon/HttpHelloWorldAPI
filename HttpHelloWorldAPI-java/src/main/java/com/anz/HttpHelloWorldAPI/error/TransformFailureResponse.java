/**
 * 
 */
package com.anz.HttpHelloWorldAPI.error;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.anz.common.compute.ComputeInfo;
import com.anz.common.compute.impl.ComputeUtils;
import com.anz.common.dataaccess.models.iib.ErrorStatusCode;
import com.anz.common.domain.ErrorStatusCodeDomain;
import com.anz.common.error.ExceptionMessage;
import com.anz.common.transform.ITransformer;
import com.anz.common.transform.TransformUtils;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;

/**
 * @author sanketsw
 * 
 */
public class TransformFailureResponse implements
		ITransformer<MbMessageAssembly, String> {
			
	private static final Logger logger = LogManager.getLogger();

	@Override
	public String execute(MbMessageAssembly outAssembly, Logger appLogger,
			ComputeInfo metadata) throws Exception {
		
		logger.entry();
		
		String out = null;

		String exceptionText = ComputeUtils.getExceptionText(outAssembly);
		logger.info("exceptionText {} ", exceptionText);

		// This could be the business or HTTP Request exception
		MbMessage outMessage = outAssembly.getMessage();
		String messageString = ComputeUtils.getStringFromBlob(outMessage);
		
		String errorString = null;
		if(exceptionText == null && messageString== null) {
			// This is a timeout on MQ
			errorString = ErrorStatusCode.TimeoutException;
		} else {	
			errorString = ErrorStatusCode.InternalException;
		}

		// Log the input blob
		logger.info("inputString {} ", messageString);

		ExceptionMessage exceptionMessage = new ExceptionMessage();
		exceptionMessage.setId(metadata.getUserDefinedProperties().get("Transaction-Id"));
		exceptionMessage.setTimestamp(Calendar.getInstance().getTime());
		exceptionMessage.setShortException(exceptionText);
		exceptionMessage.setMessage(messageString);
		exceptionMessage.setBrokerAndServiceDetails(metadata);
		exceptionMessage.setStaticProperties();	
		
		// Return the error after mapping errorCode from cache/database
		ErrorStatusCode errorCode = ErrorStatusCodeDomain.getInstance().getErrorCode(errorString);
				
		// If error code cannot be mapped, then return the original error
		if (errorCode != null) {
			exceptionMessage.setStatus(errorCode);
		}

		out = TransformUtils.toJSON(exceptionMessage);
		logger.info("Error Status Code object {}", out);
		appLogger.error(out);
		return out;
	}

}
