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

if($listname == "clubtable"){
    $sql1 = "delete from clubtable where idx = '$idx'";
    $sql2 = "delete from clubextension where postidx = '$idx'";
    $sql3 = "delete from postlist where postidx = '$idx' AND listname = '$listname' AND username = '$username'";
} else if($listname == "freelancer"){
    $sql1 = "delete from freelancer where idx = '$idx'";
    $sql2 = "delete from freeextension where postidx = '$idx'";
    $sql3 = "delete from postlist where postidx = '$idx' AND listname = '$listname' AND username = '$username'";
} else if($listname == "competition"){
    $sql1 = "delete from competition where idx = '$idx'";
    $sql2 = "delete from competitionextension where postidx = '$idx'";
    $sql3 = "delete from postlist where postidx = '$idx' AND listname = '$listname' AND username = '$username'";
} else if($listname == "dongho"){
    $sql1 = "delete from dongho where idx = '$idx'";
    $sql2 = "delete from donghoextension where postidx = '$idx'";
    $sql3 = "delete from postlist where postidx = '$idx' AND listname = '$listname' AND username = '$username'";
} else if($listname == "review"){
    $sql1 = "delete from review where idx = '$idx'";
    $sql2 = "delete from reviewextension where postidx = '$idx'";
    $sql3 = "delete from postlist where postidx = '$idx' AND listname = '$listname' AND username = '$username'";
} else if($listname == "employment"){
    $sql1 = "delete from employment where idx = '$idx'";
    $sql2 = "delete from employmentextension where postidx = '$idx'";
    $sql3 = "delete from postlist where postidx = '$idx' AND listname = '$listname' AND username = '$username'";
}

$result1 = mysqli_query($link,$sql1);
$result2 = mysqli_query($link,$sql2);
$result3 = mysqli_query($link,$sql3);

if($result1 AND $result2 AND $result3){
    }
else{
   echo "SQL문 처리중 에러 발생 : ";
   echo mysqli_error($link);
}

mysqli_close($link);
?>