<?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

//$link=mysqli_connect("localhost","root id","root pw","db name");
$link=mysqli_connect("localhost","spotz","tongood77","spotz");
if (!$link)  
{ 
   echo "MySQL 접속 에러 : ";
   echo mysqli_connect_error();
   exit();
}  

mysqli_set_charset($link,"utf8");  

//POST 값을 읽어온다.
$title=isset($_POST['title']) ? $_POST['title'] : '';
$contents=isset($_POST['contents']) ? $_POST['contents'] : '';
$username=isset($_POST['username']) ? $_POST['username'] : '';
$image1=isset($_POST['image1']) ? $_POST['image1'] : '';
$image2=isset($_POST['image2']) ? $_POST['image2'] : '';
$image3=isset($_POST['image3']) ? $_POST['image3'] : '';
$listname=isset($_POST['listname']) ? $_POST['listname'] : '';

if ($title !="" and $contents !="" ){
    if($listname == 'clubtable'){
        $sql="update clubtable set title = '$title', contents = '$contents', image1 ='$image1', image2 ='$image2', image3 ='$image3' WHERE username = '$username'"; 
    } else if ($listname == 'freelancer'){
        $sql="update freelancer set title = '$title', contents = '$contents', image1 ='$image1', image2 ='$image2', image3 ='$image3' WHERE username = '$username'"; 
    }
    $result=mysqli_query($link,$sql);  
    if($result){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($link);
    }

} else {
    echo "데이터를 입력하세요 ";
}


mysqli_close($link);
?>

<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <head><meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
   <body>
   
      <form action="<?php $_PHP_SELF ?>" method="POST">
         Title: <input type = "text" name = "title" />
         contents: <input type = "text" name = "contents" />
         <input type = "submit" />
      </form>
   
   </body>
</html>
<?php
}
?>
