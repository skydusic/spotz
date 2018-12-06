<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77");
    if (!$connect) {
        $error = mysqli_connect_error();
        $errno = mysqli_connect_errno(); 
        die("Connection failed : "."$errno : $error\n");
        exit(); 
    }

    mysqli_set_charset($connect,"utf-8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $idx=isset($_POST['idx']) ? $_POST['idx'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';

    if($listname == "freeboard"){
        $sql ="select * from freeboard where idx = '$idx'";
    } else if($listname == "kbl"){
        $sql ="select * from kbl where idx = '$idx'";
    } else if($listname == "nba"){
        $sql ="select * from nba where idx = '$idx'";
    } else if($listname == "equip"){
        $sql ="select * from equip where idx = '$idx'";
    } else if($listname == "employ"){
        $sql ="select * from employ where idx = '$idx'";
    } else if($listname == "compet"){
        $sql ="select * from compet where idx = '$idx'";
    }
    
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
        mysqli_data_seek($result, $i);
        $row = mysqli_fetch_array($result);
        
        if($listname == "freeboard"){
            echo "{\"listname\":\"freeboard\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata\":\"$row[spindata]\"}";
        } else if ($listname == "kbl"){
            echo "{\"listname\":\"kbl\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata\":\"$row[spindata]\"}";
        } else if ($listname == "nba"){
            echo "{\"listname\":\"nba\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata\":\"$row[spindata]\"}";
        } else if ($listname == "equip"){
            echo "{\"listname\":\"equip\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata\":\"$row[spindata]\"}";
        } else if ($listname == "employ"){
            echo "{\"listname\":\"employ\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata\":\"$row[spindata]\"}";
        } else if ($listname == "compet"){
            echo "{\"listname\":\"compet\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata\":\"$row[spindata]\"}";
        }
        
        if($i<$total_record-1){
          echo ",";
        }
    }

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>