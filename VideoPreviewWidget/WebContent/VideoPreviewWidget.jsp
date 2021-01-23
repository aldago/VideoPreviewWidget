<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Test Widget</title>
		<link href="resources/video-js.css" rel="stylesheet" />
		<script src="resources/video.min.js"></script>
		
		<script src="resources/OpenAjaxManagedHub-all.js"></script>
		<script src="resources/D2-OAH.js"></script>
		
		<script type="text/javascript">
		            var ticket = "";
		            var user = "";
		            var docbase = "";
		            var objectId = "";
		            var playURL = "";
		
		            function parseUrlString() {
		                var query = window.location.search.substring(1);
		                var temp = query.split("&");
		                for (var i = 0; i < temp.length; i++) {
		
		                    var k = temp[i].substring(0, temp[i].indexOf("="));
		                    var v = temp[i].substring(temp[i].indexOf("=") + 1, temp[i].length);
		
		                    if (k === "user") {
		                        user = v;
		                    }
		                    if (k === "docbase") {
		                        docbase = v;
		                    }
		                    if (k === "ticket") {
		                        ticket = v;
		                    }
		                }
		            }
		
		            /* Callback that is invoked upon successful connection to the Managed Hub */
		            function connectCompleted(hubClient, success, error) {
		                if (success) {
		                    d2OpenAjaxHub.subscribeToChannel("D2_EVENT_SELECT_OBJECT", onSelectObject, true);
		                }   
		            }
		
		            /* Callback that is invoked upon widget activation*/
		            function onActiveWidget(bActiveFlag) {
		            }
		
		            /* Application initializes in response to document load event */
		            function init() {
		                parseUrlString();
		                d2OpenAjaxHub = new D2OpenAjaxHub();
		                d2OpenAjaxHub.connectHub(connectCompleted, onActiveWidget);
		            }
		
		
		            /* Callback that is invoked when object is generated*/
		            function onSelectObject(event, oMessage){
		            	objectId=oMessage.getId();
		            	
		            	playUrl="http://127.0.0.1:8080/D2/servlet/Download?_docbase=" + docbase +"&_locale=en&_username=" + user + "&_password=" + ticket + "&id=" + objectId + "&format=_DEFAULT_&event_name=d2_view&content_disposition=inline";
		            	//console.log("http://127.0.0.1:8080/D2/servlet/Download?_docbase=" + docbase +"&_locale=en&_username=" + user + "&_password=" + ticket + "&id=" + objectId + "&format=_DEFAULT_&event_name=d2_view&content_disposition=inline");
		            	
		            	var video = videojs("vid1");
		            	video.src({
		            		  //type: 'video/mp4',
		            		  src: playUrl
		            		});
		            }
		</script>
	</head>
	<body onload="init();">
		<video id="vid1" class="video-js" controls preload="auto"
			data-setup='{"fluid": true}'>
			<source src="" type="video/mp4" />
			<p class="vjs-no-js">
				To view this video please enable JavaScript, and consider upgrading
				to a web browser that <a
					href="https://videojs.com/html5-video-support/" target="_blank">supports
					HTML5 video</a>
			</p>
		</video>
	</body>
</html>