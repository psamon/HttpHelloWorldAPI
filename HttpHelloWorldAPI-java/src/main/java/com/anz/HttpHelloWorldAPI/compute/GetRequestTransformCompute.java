/**
 * 
 */
package com.anz.HttpHelloWorldAPI.compute;

import com.anz.HttpHelloWorldAPI.transform.PreTransformBLSample;
import com.anz.HttpHelloWorldAPI.transform.TransformBLSample;
import com.anz.common.compute.ComputeInfo;
import com.anz.common.compute.impl.CommonBlobTransformCompute;
import com.anz.common.compute.impl.ComputeUtils;
import com.anz.common.transform.ITransformer;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessageAssembly;

/**
 * @author sanketsw
 *
 */
public class GetRequestTransformCompute extends CommonBlobTransformCompute {

	/* (non-Javadoc)
	 * @see com.anz.common.compute.impl.CommonJsonJsonTransformCompute#getTransformer()
	 */
	@Override
	public ITransformer<String, String> getTransformer() {
		return new PreTransformBLSample();
	}

	@Override
	public void prepareForTransformation(ComputeInfo metadata,
			MbMessageAssembly inAssembly, MbMessageAssembly outAssembly) {
		
		// Set Method and URL to local environment
		try {
			ComputeUtils.setElementInTree("GET", outAssembly.getLocalEnvironment() ,"Destination", "HTTP", "RequestLine", "Method");
			ComputeUtils.setElementInTree(getUserDefinedAttribute("HTTP_GET_URL"), outAssembly.getLocalEnvironment() ,"Destination", "HTTP", "RequestURL");
		} catch (MbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
