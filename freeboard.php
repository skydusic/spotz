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
    $pageorder=isset($_POST['pageorder']) ? $_POST['pageorder'] : '';
    $orderNum = 15;
    $pageend = ((int)$pageorder+1) * $orderNum;
    $pageorder = (int)$pageorder * $orderNum;

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
    } else if($listname == "qna"){
        if($spindata == "전체"){
            $sql = "select * from qna order by created desc";
        } else {
            $sql = "select * from qna where spindata = '$spindata' order by created desc";
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
    $flag = $pageend - $total_record;
    echo "{\"status\":\"OK\",\"total\":\"$total_record\",\"results\":[";
    if ($total_record >= $pageorder) {
        if($total_record <= $pageend) {
            $pageend = $total_record;
        }
        for ($i=$pageorder; $i < $pageend; $i++)
        {
            mysqli_data_seek($result, $i);
            $row = mysqli_fetch_array($result);
            if($listname == "freeboard"){
                echo "{\"listname\":\"freeboard\",";
            } else if ($listname == "kbl"){
                echo "{\"listname\":\"kbl\",";
            } else if ($listname == "nba"){
                echo "{\"listname\":\"nba\",";
            } else if ($listname == "equip"){
                echo "{\"listname\":\"equip\",";
            } else if ($listname == "qna"){
                echo "{\"listname\":\"qna\",";
            } else if ($listname == "compet"){
                echo "{\"listname\":\"compet\",";
            }
            echo "\"idx\":$row[idx],\"title\":\"$row[title]\",\"username\":\"$row[username]\",\"email\":\"$row[email]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata\":\"$row[spindata]\"}";
            if($i<$pageend-1){
              echo ",";
            }
        }
        echo "],\"endpage\":\"continue\"}";
    }
    
    mysqli_free_result($result);
    mysqli_close($connect);
?>