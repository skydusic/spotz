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
$image=isset($_POST['image']) ? $_POST['image'] : '';
$listname=isset($_POST['listname']) ? $_POST['listname'] : '';
$spindata1=isset($_POST['spindata1']) ? $_POST['spindata1'] : '';
$spindata2=isset($_POST['spindata2']) ? $_POST['spindata2'] : '';
$owner=isset($_POST['owner']) ? $_POST['owner'] : '';
$timetable=isset($_POST['timetable']) ? $_POST['timetable'] : '';
$location=isset($_POST['location']) ? $_POST['location'] : '';
$traffic=isset($_POST['traffic']) ? $_POST['traffic'] : '';
$fee=isset($_POST['fee']) ? $_POST['fee'] : '';
$phone=isset($_POST['phone']) ? $_POST['phone'] : '';
$tname = isset($_POST['tname']) ? $_POST['tname'] : '';
$tcareer = isset($_POST['tcareer']) ? $_POST['tcareer'] : '';
$tetc = isset($_POST['tetc']) ? $_POST['tetc'] : '';


if ($title !="" and $contents !="" ){
    if($listname == "clubtable"){
        $sql="insert into clubtable(title,contents,username,image,spindata1,spindata2) values('$title','$contents','$username','$image','$spindata1','$spindata2')";
        $sql2="insert into clubextension (owner, timetable, location, traffic, fee, phone) values ('$owner','$timetable','$location','$traffic','$fee','$phone')";
        $sql3="insert into clubemployee (name, career, etc) values ('$tname','$tcareer','$tetc')"
    } else if ($listname == "freelancer"){
        $sql="insert into freelancer(title,contents,username,image,spindata1,spindata2) values('$title','$contents','$username','$image','$spindata1','$spindata2')";
    } else if ($listname == "competition"){
        $sql="insert into competition(title,contents,username,image,spindata1,spindata2) values('$title','$contents','$username','$image','$spindata1','$spindata2')";
    } else if ($listname == "dongho"){
        $sql="insert into dongho(title,contents,username,image,spindata1,spindata2) values('$title','$contents','$username','$image','$spindata1','$spindata2')";
    } else if ($listname == "review"){
        $sql="insert into review(title,contents,username,image,spindata1,spindata2) values('$title','$contents','$username','$image','$spindata1','$spindata2')";
    } else if ($listname == "employment"){
        $sql="insert into employment(title,contents,username,image,spindata1,spindata2) values('$title','$contents','$username','$image','$spindata1','$spindata2')";
    }
    
    $result=mysqli_query($link,$sql);  
    if($result){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($link);
    }
    
    $result2=mysqli_query($link,$sql2);  
    if($result2){
       echo "SQLex문 처리 성공";
    }
    else{
       echo "SQLex문 처리중 에러 발생 : ";
       echo mysqli_error($link);
    }
    
    $result3=mysqli_query($link,$sql3);  
    if($result3){
       echo "SQLemployee문 처리 성공";
    }
    else{
       echo "SQLemployee문 처리중 에러 발생 : ";
       echo mysqli_error($link);
    }

} else {
    echo "데이터를 입력하세요 ";
}

mysqli_close($link);
?>