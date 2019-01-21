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
$writer=isset($_POST['writer']) ? $_POST['writer'] : '';
$wemail=isset($_POST['wemail']) ? $_POST['wemail'] : '';
$created=isset($_POST['created']) ? $_POST['created'] : '';
$image=isset($_POST['image']) ? $_POST['image'] : '';
$report_contents=isset($_POST['report_contents']) ? $_POST['report_contents'] : '';
$username=isset($_POST['username']) ? $_POST['username'] : '';
$email=isset($_POST['email']) ? $_POST['email'] : '';

$sql="insert into reportTable(listname,RIdx,RTitle,RContents,RUsername,Remail,RCreated,contents,image,reporter,reportermail) values('$listname','$idx','$title','$contents','$writer','$wemail','$created','$report_contents','$image','$username','$email')";
$result=mysqli_query($link,$sql);
if($result){
   echo "SQL문 처리 성공";
}
else{
   echo "SQL문 처리중 에러 발생 : ";
   echo mysqli_error($link);
}
mysqli_close($link);
?>