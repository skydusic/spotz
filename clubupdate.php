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

if ($title !="" and $contents !="" ){
    if($listname == 'clubtable'){
        $sql="update clubtable set title = '$title', contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE username = '$username'"; 
    } else if ($listname == 'freelancer'){
        $sql="update freelancer set title = '$title', contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE username = '$username'"; 
    } else if ($listname == 'competition'){
        $sql="update competition set title = '$title', contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE username = '$username'"; 
    } else if ($listname == 'dongho'){
        $sql="update dongho set title = '$title', contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE username = '$username'"; 
    } else if ($listname == 'review'){
        $sql="update review set title = '$title', contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE username = '$username'"; 
    } else if ($listname == 'employment'){
        $sql="update employment set title = '$title', contents = '$contents', image ='$image', spindata1 = '$spindata1', spindata2 = '$spindata2' WHERE username = '$username'"; 
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