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
        <form id="loginForm" class="z-depth-1 white" method="POST" action="/rapid/login">
     	    <div class="input-field col s6">
	          <input name="login" id="login" type="text" class="validate">
	          <label for="login">Ваш логин</label>
	        </div>
        	<div class="input-field col s6">
	          <input name="password" id="password" type="password" class="validate">
	          <label for="password">Ваш пароль</label>
	        </div>
			<button class="btn waves-effect waves-light" type="submit" name="action">Войти
			  <i class="material-icons right">send</i>
			</button>
        </form>
    </body>
    
	<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="materialize/js/materialize.min.js"></script>
</html>