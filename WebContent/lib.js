	function getProgress()
	{
		$.get("/rapid/upload/progress", function(data){
			var progress = data["progress"];
			console.log(progress);
			$("#progress").val(progress);
			if ( progress != 100 )
			{
				window.setTimeout(getProgress(), 1000);
			}
		});
	}
	
	$("#form").submit(function(event){
		window.setTimeout(getProgress(), 1000);
	});
