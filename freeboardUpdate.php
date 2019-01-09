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
$idx=isset($_POST['idx']) ? $_POST['idx'] : '';
$listname=isset($_POST['listname']) ? $_POST['listname'] : '';
$title=isset($_POST['title']) ? $_POST['title'] : '';
$contents=isset($_POST['contents']) ? $_POST['contents'] : '';
$username=isset($_POST['username']) ? $_POST['username'] : '';
$image=isset($_POST['image']) ? $_POST['image'] : '';
$spindata=isset($_POST['spindata']) ? $_POST['spindata'] : '';

if($listname == 'freeboard'){
    $sql="update freeboard set title = '$title',contents = '$contents', image ='$image', spindata = '$spindata' WHERE idx = '$idx'";
} else if ($listname == 'kbl'){
    $sql="update kbl set title = '$title',contents = '$contents', image ='$image', spindata = '$spindata' WHERE idx = '$idx'";
} else if ($listname == 'nba'){
    $sql="update nba set title = '$title',contents = '$contents', image ='$image', spindata = '$spindata' WHERE idx = '$idx'";
} else if ($listname == 'equip'){
    $sql="update equip set title = '$title',contents = '$contents', image ='$image', spindata = '$spindata' WHERE idx = '$idx'";
} else if ($listname == 'qna'){
    $sql="update qna set title = '$title',contents = '$contents', image ='$image', spindata = '$spindata' WHERE idx = '$idx'";
} else if ($listname == 'compet'){
    $sql="update compet set title = '$title',contents = '$contents', image ='$image', spindata = '$spindata' WHERE idx = '$idx'";
}
$result=mysqli_query($link,$sql);  
if($result){
   echo "update 처리 성공";
}
else{
   echo "update 처리중 에러 발생 : ";
   echo mysqli_error($link);
}

mysqli_close($link);
?>