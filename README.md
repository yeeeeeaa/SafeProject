# SafeProject
### : 1인 탄소배출량 줄이기 프로젝트   
 : 아래에 기술할 세 가지 **인증** 방식(다회용컵 사용/채식 식단/걸음 수)을 수행하고    
그 결과 자신이 어느 정도의 **탄소배출량**을 **절약**했는지 직접 확인하여 **환경** **문제**에 대한 관심을 고취시켜
최종적으로 환경 문제를 앓고 있는 지구를 위해 개개인이 할 수 있는 사소하지만 중요한 일에 대한 **인식**을 제공한다.  

       
## 실행 방법
 : yolov5를 clone한 파일 안에 해당 git 폴더를 넣고   
 detect.py 속에 모델 결과를 텍스트 파일에 적는 코드를 추가하고
 ```python
    with open("SafeProject/app/src/main/res/raw/res.txt", 'wb') as file:
        try:
            res = names[int(c)]
            res = res.encode('utf-8')  # 추가
            file.write(res)
        except:
            file.write("none".encode('utf-8'))
```
<br/>
   
파이썬 코드를 통한 결과 데이터를 안드로이드 스튜디오와 송수신하기 위한 flask 코드 파일을 만들어 실행시킨 뒤 안드로이드 스튜디오 앱을 실행시키면 AI모델이 작동한다.
```python
from urllib import request
from flask import Flask, request
from flask_restx import Resource, Api
from werkzeug.utils import secure_filename

import os

# Flask 인스턴스 정리
app = Flask(__name__)
api = Api(app)

@api.route('/save/food')
class uploadging(Resource):
    def post(self):
        f = request.files['food']
        f.save(secure_filename(f'food.jpg'))
        os.system('cd yolov5 폴더 경로')
        os.system('python detect.py --weights "SafeProject/app/src/main/assets/best_food.tflite" --source "food.jpg"')
    
        return {"isUploadSuccess" : "success"}
    
@api.route('/save/cup')
class uploadging(Resource):
    def post(self):
        f = request.files['cup']
        f.save(secure_filename(f'cup.jpg'))
        os.system('cd yolov5 폴더 경로')
        os.system('python detect.py --weights "SafeProject/app/src/main/assets/best_cup.tflite" --source "cup.jpg"')
    
        return {"isUploadSuccess" : "success"}

class result(Resource):
    def get(self):
        file = open('SafeProject/app/src/main/res/raw/res.txt', 'r')
        res = file.read()
        print(res)
        file.close()
        return res
    
api.add_resource(result, '/res')

if __name__ == '__main__':
    app.run(host="ip주소", port=8080, debug=True)  
```

<br/>
   
---

<img src="https://github.com/user-attachments/assets/13365e60-a0b7-47dc-8863-aaf204820709" width="300" height="500"/>
            
### 1. 다회용컵 사용 인증
### 2. 채식 인증
### 3. 걸음 수 인증 

<br/>

---
   

## 1. 다회용컵 사용 인증

Yolov5 모델을 활용하여 [머그컵 이미지 데이터셋](https://universe.roboflow.com/house-vbmq0/mug-achwt)을 삽입하여 AI모델을 학습시킨다.

학습시킨 모델은 애플리케이션 사용자가 인증 사진을 업로드하면 Flask와 연동하여 파이썬 코드를 실행한 뒤, detecting 결과를 텍스트 파일에 저장해 **None** 문자열을 확인하면 채식 인증을 성공할 수 있게 설계하였다.

   <img src="https://github.com/user-attachments/assets/3fcb5753-38e6-4ed6-bfee-5ba8049066ca" width="300" height="500"/>
   <img src="https://github.com/user-attachments/assets/842cce3c-6512-4f4b-953d-25a82f9c62aa" width="300" height="500"/>
<img src="https://github.com/user-attachments/assets/a9d15abe-bd24-4f4b-a82f-30189187ac02" width="300" height="500"/>

   <br/>

---
   
## 2. 채식 인증

Yolov5 모델을 활용하여 [AI허브의 음식 이미지 데이터셋](https://aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=realm&dataSetSn=74) 속 계란찜, 조기구이, 육회 등의 고기를 포함한 식단 사진을 따로 다운로드한 뒤 AI모델을 학습시킨다.

학습시킨 모델은 다회용컵 인증과 같은 방식으로 인증을 확인한다.   

<br/>
   
---

## 3. 걸음 수 인증

안드로이드 스튜디오 내장 sensor를 사용하여 걸음 수를 측정하고, 측정한 걸음 수를 거리로 환산하여 절약한 탄소배출량을 계산해 업로드한다.
   
   <img src="https://github.com/user-attachments/assets/e660987c-23c1-4296-af11-be48e9c1aabe" width="300" height="500"/>
<img src="https://github.com/user-attachments/assets/01f06c29-5a56-4390-8fd6-a8dc28492319" width="300" height="500"/>

<br/>

---

## 그외
### 로그인 및 회원가입 기능
: 로그인 및 회원가입은 파이어베이스의 Auth 기능을 이용하여 개발하였다.

<img src="https://github.com/user-attachments/assets/7a753ef6-ac91-427c-8676-30fcb0b87847" width="300" height="500"/>
<img src="https://github.com/user-attachments/assets/6642f02f-7a41-4719-b05b-a1097cc02146" width="300" height="500"/>

   <br/>

### 홈 화면
: 현재 로그인한 계정의 이번 주 동안 인증한 데이터를 확인해 볼 수 있다. 절약한 탄소배출량에 따른 캐릭터와 그 캐릭터에 해당하는 설명이 같이 화면에 출력된다.

   <img src="https://github.com/user-attachments/assets/4116c1f4-082f-4aec-87f2-0d1d986f344f" width="300" height="500"/>


<br/>

### 사용자 목록
: 이 애플리케이션을 사용하는 다른 사용자들의 닉네임과 이번 주 절약한 탄소배출량, 그리고 그에 해당하는 캐릭터들이 함께 리스트뷰에 출력된다. 이를 통해 사용자는 자신보다 많이 절약한 사용자를 보고 자극을 받거나, 곧 자신을 따라잡을 것 같은 사용자를 보고 의지를 다질 수 있다.

   <img src="https://github.com/user-attachments/assets/259e3f4e-8192-4634-a797-264229f195cd" width="300" height="500"/>

<br/>

### 지난 주 정보
: 현재 로그인한 계정이 지난 주에 인증한 데이터를 바탕으로 계산된 절약한 탄소배출량을 확인할 수 있고, 이에 따른 캐릭터와 그에 해당하는 설명이 같이 화면에 출력된다.

<img src="https://github.com/user-attachments/assets/ae974e20-9ae7-4f16-b634-717ca10d638f" width="300" height="500"/>
   
<br/>
