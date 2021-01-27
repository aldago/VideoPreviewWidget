package com.tests;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfEnumeration;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.acs.IDfAcsRequest;
import com.documentum.fc.client.acs.IDfAcsTransferPreferences;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfList;
import com.documentum.fc.common.IDfLoginInfo;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;

/**
 * Servlet implementation class GetACSLinkServlet
 */
@WebServlet("/GetACSLinkServlet")
public class GetACSLinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetACSLinkServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("user");
		String ticket = request.getParameter("ticket");
		String docbase = request.getParameter("docbase");
		String objectId = request.getParameter("objectId");
		String docURL = null;
		String format = null;
		
		IDfSessionManager sessionMgr = null;
		IDfSession session = null;

		try {
			IDfLoginInfo li = new DfLoginInfo();
			li.setUser(user);
			li.setPassword(ticket);
			li.setDomain("");
			
			IDfClientX clientx = new DfClientX();  
			sessionMgr = clientx.getLocalClient().newSessionManager();
			if (sessionMgr != null){
				sessionMgr.setIdentity(docbase, li);
				session = sessionMgr.getSession(docbase);

			    IDfDocument video=(IDfDocument)session.getObject(new DfId(objectId));  
			   
			    IDfAcsTransferPreferences atp = clientx.getAcsTransferPreferences();  
			    atp.preferAcsTransfer(true);  
			   
			    IDfExportOperation exportOp = clientx.getExportOperation();  
			    exportOp.setAcsTransferPreferences(atp);  
			    IDfExportNode exportNode = (IDfExportNode) exportOp.add(video);  
			    boolean result = exportOp.execute();  
			   
			    if (result) {  
			        IDfList nodes = exportOp.getNodes();  
			        for (int i = 0, size = nodes.getCount(); i < size; i++) {  
			            IDfExportNode node = (IDfExportNode) nodes.get(i);  
			            IDfEnumeration acsRequests = node.getAcsRequests();  
			            while (acsRequests.hasMoreElements()) {  
			                IDfAcsRequest acsRequest = (IDfAcsRequest) acsRequests.nextElement();  
			                docURL = acsRequest.makeURL();                     
			            }  
			        }  
			    }    

			    format=getFormat(session,video);
			}
			
			JSONObject json = new JSONObject();
		    json.put("url", docURL);
		    json.put("format", format);
		    
			response.setContentType("application/json");
			response.getWriter().write(json.toString());
		}catch(DfException ex) {
			ex.printStackTrace();
		}finally {
			try {
				session.disconnect();
				sessionMgr.clearIdentities();
			} catch (DfException e) {
				e.printStackTrace();
			}

		}
	}
	
	private String getFormat(IDfSession session, IDfDocument video) {
		//Do whatever logic required
		return "mp4";
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
