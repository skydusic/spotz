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
$listname=isset($_POST['listname']) ? $_POST['listname'] : '';
$idx=isset($_POST['idx']) ? $_POST['idx'] : '';
$title=isset($_POST['title']) ? $_POST['title'] : '';
$contents=isset($_POST['contents']) ? $_POST['contents'] : '';
$username=isset($_POST['username']) ? $_POST['username'] : '';
$image=isset($_POST['image']) ? $_POST['image'] : '';
$spindata=isset($_POST['spindata']) ? $_POST['spindata'] : '';

if($listname == 'freeboard'){
    $sql="update freeboard set title = '$title',contents = '$contents', image ='$image', spindata = '$spindata' WHERE idx = '$idx'";
} else if ($listname == 'freelancer'){
    $sql="update freelancer set contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE idx = '$idx'";
    $sql2="update freeextension set name = '$text1', sports = '$text2', location = '$text3', phone = '$text4', etc = '$text5' WHERE postidx = '$idx'";
} else if ($listname == 'competition'){
    $sql="update competition set contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE idx = '$idx'";
    $sql2="update competitionextension set name = '$text1', sports = '$text2', location = '$text3', phone = '$text4', etc = '$text5' WHERE postidx = '$idx'";
} else if ($listname == 'dongho'){
    $sql="update dongho set contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE idx = '$idx'";
    $sql2="update donghoextension set name = '$text1', sports = '$text2', location = '$text3', time = '$text4', phone = '$text5' WHERE postidx = '$idx'";
} else if ($listname == 'review'){
    $sql="update review set contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE idx = '$idx'";
    $sql2="update reviewextension set product = '$text1', wheretobuy = '$text2', price = '$text3', grade = '$text4', etc = '$text5' WHERE postidx = '$idx'";
} else if ($listname == 'employment'){
    $sql="update employment set contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE idx = '$idx'";
    $sql2="update employmentextension set company = '$text1', location = '$text2', salary = '$text3', calendar = '$text4', etc = '$text5' WHERE postidx = '$idx'";
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