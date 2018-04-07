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
$contents=isset($_POST['contents']) ? $_POST['contents'] : '';
$username=isset($_POST['username']) ? $_POST['username'] : '';
$image=isset($_POST['image']) ? $_POST['image'] : '';
$listname=isset($_POST['listname']) ? $_POST['listname'] : '';
$spindata1=isset($_POST['spindata1']) ? $_POST['spindata1'] : '';
$spindata2=isset($_POST['spindata2']) ? $_POST['spindata2'] : '';

$text1=isset($_POST['text1']) ? $_POST['text1'] : '';
$text2=isset($_POST['text2']) ? $_POST['text2'] : '';
$text3=isset($_POST['text3']) ? $_POST['text3'] : '';
$text4=isset($_POST['text4']) ? $_POST['text4'] : '';
$text5=isset($_POST['text5']) ? $_POST['text5'] : '';

if($listname == "clubtable"){
    $sql="insert into clubtable(contents,username,image,spindata1,spindata2) values('$contents','$username','$image','$spindata1','$spindata2')";
} else if ($listname == "freelancer"){
    $sql="insert into freelancer(contents,username,image,spindata1,spindata2) values('$contents','$username','$image','$spindata1','$spindata2')";
} else if ($listname == "competition"){
    $sql="insert into competition(contents,username,image,spindata1,spindata2) values('$contents','$username','$image','$spindata1','$spindata2')";
} else if ($listname == "dongho"){
    $sql="insert into dongho(contents,username,image,spindata1,spindata2) values('$contents','$username','$image','$spindata1','$spindata2')";
} else if ($listname == "review"){
    $sql="insert into review(contents,username,image,spindata1,spindata2) values('$contents','$username','$image','$spindata1','$spindata2')";
} else if ($listname == "employment"){
    $sql="insert into employment(contents,username,image,spindata1,spindata2) values('$contents','$username','$image','$spindata1','$spindata2')";
}

$result=mysqli_query($link,$sql);  
if($result){
   echo "SQL문 처리 성공";
}
else{
   echo "SQL문 처리중 에러 발생 : ";
   echo mysqli_error($link);
}

if($listname == "clubtable"){
    $temp = "select * from clubtable where username = '$username' order by created desc";
    $result = mysqli_query($link,$temp);
    mysqli_data_seek($result, 0);
    $row = mysqli_fetch_array($result);

    $sql2="insert into clubextension (postidx, corperation, sports, location, phone, etc) values ('$row[idx]','$text1','$text2','$text3','$text4','$text5')";
} else if ($listname == "freelancer"){

} else if ($listname == "competition"){

} else if ($listname == "dongho"){

} else if ($listname == "review"){

} else if ($listname == "employment"){

}

$result2=mysqli_query($link,$sql2);  
if($result2){
   echo "SQLex문 처리 성공";
}
else{
   echo "SQLex문 처리중 에러 발생 : ";
   echo mysqli_error($link);
}

mysqli_close($link);
?>