<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <title>File Upload</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--Import Google Icon Font-->
	    <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	    <!--Import materialize.css-->
	    <link type="text/css" rel="stylesheet" href="materialize/css/materialize.min.css"  media="screen,projection"/>
   	    <link type="text/css" rel="stylesheet" href="style.css"/>
    </head>
    <body class="grey lighten-4">
    	<div class="uploadBlock z-depth-1 white">
	        <form id="fileForm" method="POST" action="/rapid/upload" enctype="multipart/form-data" >
			    <div class="file-field input-field">
			      <div class="btn">
			        <span>Файл</span>
			        <input name="file" type="file">
			      </div>
			      <div class="file-path-wrapper">
			        <input class="file-path validate" type="text">
			      </div>
			    </div>
			    <button class="btn waves-effect waves-light btn-margin" type="submit" name="action">Загрузить
				  <i class="material-icons right">send</i>
				</button>
			</form>
			<div class=progress-block>
		        <div id="progressPercent">0%</div>
		        <progress value="0" max="100" id="progress"></progress>
	        </div>
	    </div>
    </body>
   	<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
  	<script type="text/javascript" src="lib.js"></script>
	<script type="text/javascript" src="materialize/js/materialize.min.js"></script>
</html>