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
    $spindata=isset($_POST['spindata']) ? $_POST['spindata'] : '';


    if($listname == "freeboard"){
        if($spindata == "전체"){
            $sql = "select * from freeboard order by created desc";
        } else {
            $sql = "select * from freeboard where spindata = '$spindata' order by created desc";
        }
    } else if($listname == "kbl"){
        if($spindata == "전체"){
            $sql = "select * from kbl order by created desc";
        } else {
            $sql = "select * from kbl where spindata = '$spindata' order by created desc";
        }
    } else if($listname == "nba"){
        if($spindata == "전체"){
            $sql = "select * from nba order by created desc";
        } else {
            $sql = "select * from nba where spindata = '$spindata' order by created desc";
        }
    } else if($listname == "equip"){
        if($spindata == "전체"){
            $sql = "select * from equip order by created desc";
        } else {
            $sql = "select * from equip where spindata = '$spindata' order by created desc";
        }
    } else if($listname == "employ"){
        if($spindata == "전체"){
            $sql = "select * from employ order by created desc";
        } else {
            $sql = "select * from employ where spindata = '$spindata' order by created desc";
        }
    } else if($listname == "compet"){
        if($spindata == "전체"){
            $sql = "select * from compet order by created desc";
        } else {
            $sql = "select * from compet where spindata = '$spindata' order by created desc";
        }
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