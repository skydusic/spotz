<?php
error_reporting(E_ALL); 
ini_set('display_errors',1); 

$link=mysqli_connect("localhost","spotz","tongood77","spotz");
if (!$link)
{
   echo "MySQL 접속 에러 : ";
   echo mysqli_connect_error();
   exit();
}

mysqli_set_charset($link,"utf8");  

$idx=isset($_POST['idx']) ? $_POST['idx'] : '';
$listname=isset($_POST['listname']) ? $_POST['listname'] : '';
$username=isset($_POST['username']) ? $_POST['username'] : '';
$title=isset($_POST['title']) ? $_POST['title'] : '';
$contents=isset($_POST['contents']) ? $_POST['contents'] : '';
$created=isset($_POST['created']) ? $_POST['created'] : '';


if($username == 'Duil Song'){
    $removername = "admin";
} else {
    $removername = "customer";
}

if($listname == "freeboard"){
    $sql1 = "delete from freeboard where idx = '$idx'";
} else if($listname == "kbl"){
    $sql1 = "delete from kbl where idx = '$idx'";
} else if($listname == "nba"){
    $sql1 = "delete from nba where idx = '$idx'";
} else if($listname == "equip"){
    $sql1 = "delete from equip where idx = '$idx'";
} else if($listname == "employ"){
    $sql1 = "delete from employ where idx = '$idx'";
} else if($listname == "compet"){
    $sql1 = "delete from compet where idx = '$idx'";
}

$sql2 = "delete from postlist where postidx = '$idx' AND listname = '$listname' AND username = '$username'";

$delboardIns="insert into DeleteTable(title,contents,username,created,listname,remover) values('$title','$contents','$username','$created','$listname','$removername')";

$result1 = mysqli_query($link,$delboardIns);
$result2 = mysqli_query($link,$sql1);
$result3 = mysqli_query($link,$sql2);

if($result1 AND $result2 AND $result3){
}
else{
   echo "SQL문 처리중 에러 발생 : ";
   echo mysqli_error($link);
}

mysqli_close($link);
?>