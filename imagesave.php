<?php
    /*$connect=mysql_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");
    mysql_query("SET NAMES UTF-8");*/
    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    $username = $username."/";
    $file_path = "userImageFolder/".$listname."/";
    chdir($file_path);

    if(!is_dir("$username")){
        mkdir("$username", 0755, false);
    }
    chdir('../');
    chdir('../');
    
    $allowed_ext = array('jpg','jpeg','png','gif');

    // 변수 정리
    $error = $_FILES['uploaded_file']['error'];
    $name = $_FILES['uploaded_file']['name'];
    $exp = explode('.', $name);
    $ext = array_pop($exp);

    // 오류 확인
    if( $error != UPLOAD_ERR_OK ) {
        switch( $error ) {
            case UPLOAD_ERR_INI_SIZE:
            case UPLOAD_ERR_FORM_SIZE:
                echo "파일이 너무 큽니다. ($error)";
                break;
            case UPLOAD_ERR_NO_FILE:
                echo "파일이 첨부되지 않았습니다. ($error)";
                break;
            default:
                echo "파일이 제대로 업로드되지 않았습니다. ($error)";
        }
        exit;
    }

    // 확장자 확인
    if( !in_array($ext, $allowed_ext) ) {
        echo "허용되지 않는 확장자입니다.";
        exit;
    }
     
    $file_path = $file_path . $username . basename($_FILES['uploaded_file']['name']);
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        echo "success";
    } else{
        echo "fail";
    }
 
 ?>

