<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77");
    if (!$connect) {
        $error = mysqli_connect_error();
        $errno = mysqli_connect_errno(); 
        die("Connection failed : "."$errno : $error\n");
        exit(); 
    }

    mysqli_set_charset($connect,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $postidx=isset($_POST['postidx']) ? $_POST['postidx'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';

    if($listname == "freeboard"){
        $sql="select * from freeboardCO where postidx='$postidx'";
    } else if($listname == "kbl"){
        $sql="select * from kblCO where postidx='$postidx'";
    } else if($listname == "nba"){
        $sql="select * from nbaCO where postidx='$postidx'";
    } else if($listname == "equip"){
        $sql="select * from equipCO where postidx='$postidx'";
    } else if($listname == "employ"){
        $sql="select * from employCO where postidx='$postidx'";
    } else if($listname == "compet"){
        $sql="select * from competCO where postidx='$postidx'";
    }

    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);


    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";


    for ($i=0; $i < $total_record; $i++)
    {
        mysqli_data_seek($result, $i);
        $row = mysqli_fetch_array($result);
        
        echo "{\"listname\":\"commentBoard\",\"idx\":\"$row[idx]\",\"username\":\"$row[username]\",\"postidx\":\"$row[postidx]\",\"contents\":\"$row[contents]\",\"created\":\"$row[created]\",\"commentidx\":\"$row[commentidx]\"}";
        
        
        if($i<$total_record-1){
          echo ",";
        }
        
    }

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>