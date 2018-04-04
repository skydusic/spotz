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
    } else if($listname == "competition"){
        if($spindata1 == "전국"){
            $sql ="select * from competition order by addedtime";
        } else if($spindata2 != ""){
            $sql = "select * from competition where spindata2 = '$spindata2' order by addedtime";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from competition where spindata1 = '$spindata1' order by addedtime";
        }
    } else if($listname == "dongho"){
        if($spindata1 == "전국"){
            $sql ="select * from dongho order by addedtime";
        } else if($spindata2 != ""){
            $sql = "select * from dongho where spindata2 = '$spindata2' order by addedtime";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from dongho where spindata1 = '$spindata1' order by addedtime";
        }
    } else if($listname == "review"){
        if($spindata1 == "전국"){
            $sql ="select * from review order by addedtime";
        } else if($spindata2 != ""){
            $sql = "select * from review where spindata2 = '$spindata2' order by addedtime";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from review where spindata1 = '$spindata1' order by addedtime";
        }
    } else if($listname == "employment"){
        if($spindata1 == "전국"){
            $sql ="select * from employment order by addedtime";
        } else if($spindata2 != ""){
            $sql = "select * from employment where spindata2 = '$spindata2' order by addedtime";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from employment where spindata1 = '$spindata1' order by addedtime";
        }
    }

    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
        mysqli_data_seek($result, $i);
        $row = mysqli_fetch_array($result);
        
        if($listname == "clubtable"){
            $sql2 = "select * from clubextension where idx = '$row[idx]'";
            $result2 = mysqli_query($connect,$sql2);
            mysqli_data_seek($result2, 0);
            $row2 = mysqli_fetch_array($result2);
            $sql3 = "select *from clubemployee where idx = '$row[idx]'";
            $result3 = mysqli_query($connect,$sql3);
            mysqli_data_seek($result3, 0);
        }
        echo "{\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"owner\":\"$row2[owner]\",\"timetable\":\"$row2[timetable]\",\"location\":\"$row2[location]\",\"traffic\":\"$row2[traffic]\",\"fee\":\"$row2[fee]\",\"phone\":\"$row2[phone]\",\"tname\":\"$row2[name]\",\"tcareer\":\"$row2[career]\",\"etc\":\"$row2[etc]\"}";

        if($i<$total_record-1){
          echo ",";
        }
    }

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>