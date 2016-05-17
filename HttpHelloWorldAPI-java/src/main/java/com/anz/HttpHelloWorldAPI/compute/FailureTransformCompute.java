/**
 * 
 */
package com.anz.HttpHelloWorldAPI.compute;

import com.anz.HttpHelloWorldAPI.error.TransformFailureResponse;
import com.anz.common.compute.ComputeInfo;
import com.anz.common.compute.TransformType;
import com.anz.common.compute.impl.CommonErrorTransformCompute;
import com.anz.common.transform.ITransformer;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessageAssembly;

/**
 * @author root
 *
 */
public class FailureTransformCompute extends CommonErrorTransformCompute {

	@Override
	public ITransformer<MbMessageAssembly, String> getTransformer() {
		return new TransformFailureResponse();
	}

	@Override
	public TransformType getTransformationType() {
		return TransformType.HTTP_HHTP;
	}

	@Override
	public void prepareForTransformation(ComputeInfo metadata,
			MbMessageAssembly inAssembly, MbMessageAssembly outAssembly) {
		
		try {
			MbElement transactionId = inAssembly.getMessage().getRootElement().getFirstElementByPath("/HTTPInputHeader/Transaction-Id");
			if(transactionId != null) {
				metadata.addUserDefinedProperty("Transaction-Id", transactionId.getValueAsString());
			}
		} catch (MbException e) {
			logger.throwing(e);
		}
		
	}

}
