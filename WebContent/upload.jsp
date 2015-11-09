<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <title>File Upload</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <form id="form" method="POST" action="/rapid/upload" enctype="multipart/form-data" >
            File:
            <input type="file" name="file" id="file" /> <br/>
            </br>
            <input type="submit" value="Upload" name="upload" id="upload" />
        </form>
        <div id="progressPercent">0%</div>
        <progress value="0" max="100" id="progress"></progress>
    </body>
    <script type="text/javascript" src="jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="lib.js"></script>
</html>