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
$contents=isset($_POST['contents']) ? $_POST['contents'] : '';
$username=isset($_POST['username']) ? $_POST['username'] : '';
$image=isset($_POST['image']) ? $_POST['image'] : '';
$listname=isset($_POST['listname']) ? $_POST['listname'] : '';
$spindata1=isset($_POST['spindata1']) ? $_POST['spindata1'] : '';
$spindata2=isset($_POST['spindata2']) ? $_POST['spindata2'] : '';
$created=isset($_POST['created']) ? $_POST['created'] : '';

$text1=isset($_POST['text1']) ? $_POST['text1'] : '';
$text2=isset($_POST['text2']) ? $_POST['text2'] : '';
$text3=isset($_POST['text3']) ? $_POST['text3'] : '';
$text4=isset($_POST['text4']) ? $_POST['text4'] : '';
$text5=isset($_POST['text5']) ? $_POST['text5'] : '';

if($listname == 'clubtable'){
    $sql="update clubtable set contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE idx = '$idx'";
    $sql2="update clubextension set corperation = '$text1', sports = '$text2', location = '$text3', phone = '$text4', etc = '$text5' WHERE postidx = '$idx'";
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

$result2=mysqli_query($link,$sql2);  
if($result2){
   echo "update2 처리 성공";
}
else{
   echo "update2 처리중 에러 발생 : ";
   echo mysqli_error($link);
}


mysqli_close($link);
?>