$(document).ready(function() {
	$('select').material_select();
});

function getProgress()
{
	$.get("/rapid/upload/progress", function(data){
		var progress = data["progress"];
		console.log(progress);
		
		$("#progress").val(progress);
		$("#progressPercent").html(progress + '%');
		
		if ( progress != 100 )
		{
			window.setTimeout('getProgress()', 1000);
		}
	});
}

$("#fileForm").submit(function(event){
	window.setTimeout('getProgress()', 1000);
});
