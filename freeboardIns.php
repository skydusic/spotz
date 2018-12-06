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
$spindata=isset($_POST['spindata']) ? $_POST['spindata'] : '';

if($listname == "freeboard"){
    $sql="insert into freeboard(title,contents,username,image,spindata) values('$title','$contents','$username','$image','$spindata')";
    
} else if ($listname == "kbl"){
    $sql="insert into kbl(title,contents,username,image,spindata) values('$title','$contents','$username','$image','$spindata')";
} else if ($listname == "nba"){
    $sql="insert into nba(title,contents,username,image,spindata) values('$title','$contents','$username','$image','$spindata')";
} else if ($listname == "equip"){
    $sql="insert into equip(title,contents,username,image,spindata) values('$title','$contents','$username','$image','$spindata')";
} else if ($listname == "employ"){
    $sql="insert into employ(title,contents,username,image,spindata) values('$title','$contents','$username','$image','$spindata')";
} else if ($listname == "compet"){
    $sql="insert into compet(title,contents,username,image,spindata) values('$title','$contents','$username','$image','$spindata')";
}

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