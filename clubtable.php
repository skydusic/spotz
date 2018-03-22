<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77");
    if (!$connect) {
        $error = mysqli_connect_error();
        $errno = mysqli_connect_errno(); 
        die("Connection failed : "."$errno : $error\n");
        exit(); 
    }

    mysqli_set_charset($link,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $idx=isset($_POST['idx']) ? $_POST['idx'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    $spindata1=isset($_POST['spindata1']) ? $_POST['spindata1'] : '';
    $spindata2=isset($_POST['spindata2']) ? $_POST['spindata2'] : '';

    if($listname == "clubtable"){
        if($spindata1 == "전국"){
            $sql ="select * from clubtable order by addedtime";
        } else if($spindata2 != ""){
            $sql = "select * from clubtable where spindata2 = '$spindata2' order by addedtime";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from clubtable where spindata1 = '$spindata1' order by addedtime";
        }  
    } else if($listname == "freelancer"){
        if($spindata1 == "전국"){
            $sql ="select * from freelancer order by addedtime";
        } else if($spindata2 != ""){
            $sql = "select * from freelancer where spindata2 = '$spindata2' order by addedtime";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from freelancer where spindata1 = '$spindata1' order by addedtime";
        }
    }

    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
      mysqli_data_seek($result, $i);
      $row = mysqli_fetch_array($result);
      echo "{\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"image\":\"$row[image]\"}";

    if($i<$total_record-1){
      echo ",";
    }
    }

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>