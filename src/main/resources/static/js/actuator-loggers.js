function updateLoggers(selectedLogger, host) {
        $("#error-alert").hide();
        var serviceUrl = host + "/actuator/loggers/" + selectedLogger.options[selectedLogger.selectedIndex].text;
        console.log(serviceUrl)
        $.ajax({
          url: serviceUrl,
          type: "GET",
          dataType: "json",
		  contentType : "application/json",
          success: function(response) {
            var dataStr = JSON.stringify(response);
            var jsonObj = jQuery.parseJSON(dataStr);
            $('#btn' + jsonObj.configuredLevel.toLowerCase()).prop('checked', true);
            $('#logger-info').text(jsonObj.configuredLevel);
          },
          error: function(error) {
            $('#error-alert-msg').text("Failed switching logger! error! Ensure that FTV App is up and running, " + serviceUrl);
            $("#error-alert").fadeIn();
            console.log(error)
          }
        });
  }

function changeLogLevel(selectedLoggerId, logLevel, hosts) {
    $("#error-alert").hide();
    var logger = $('#' + selectedLoggerId + ' option:selected').text();
    hosts.split(",").forEach(function(host) {
       postLogLevel(logger, logLevel, host)
    });
}

 function postLogLevel(logger, logLevel, host) {
	var serviceUrl = host + "/actuator/loggers/" + logger;
	var payload = { "configuredLevel": logLevel }
	console.log(serviceUrl + ", " + JSON.stringify(payload))
	$.ajax({
		url : serviceUrl,
		type : "POST",
		data : JSON.stringify(payload),
		dataType: "json",
		contentType : "application/json",
		mimeType : "application/json",
		cache : false,
		 headers: {
            "Authorization": "Basic er34356474ge434tfwetwt"
        },
		success : function(response) {
			$('#logger-status').text("log level changed to " + logLevel);
		},
		error : function(jqXhr, textStatus, errorThrown) {
		    $('#error-alert-msg').text("Failed changing to log level " + logLevel + " ! Ensure that FTV App is up and running, " + serviceUrl );
			console.error("Error: " + jqXhr + ", status: " + textStatus + ", REST: " + serviceUrl + ", err: " + errorThrown);
			$("#error-alert").fadeIn();
		}
	});
	return true;
}