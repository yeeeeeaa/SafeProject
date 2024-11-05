# SafeProject
: 1인 탄소배출량 줄이기 프로젝트

---

## 개발 목적
### 1. 채식 인증
### 2. 다회용컵 사용 인증
### 3. 걸음 수 인증
위의 세 가지 인증 방식을 수행하고 그 결과 자신이 어느 정도의 탄소배출량을 절약했는지 직접 확인하여 환경 문제에 대한 관심을 고취시켜 최종적으로 환경 문제를 앓고 있는 지구를 위해 개개인이 할 수 있는 사소하지만 중요한 일에 대한 인식을 제공한다.

---

## 1. 채식 인증

Yolov5 모델을 활용하여 [AI허브의 음식 이미지 데이터셋](https://aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=realm&dataSetSn=74) 속 계란찜, 조기구이, 육회 등의 고기를 포함한 식단 사진을 따로 다운로드한 뒤 AI모델을 학습시킨다.

학습시킨 모델은 애플리케이션 사용자가 인증 사진을 업로드하면 Flask와 연동하여 파이썬 코드를 실행한 뒤, detecting 결과를 텍스트 파일에 저장해 **None** 문자열을 확인하면 채식 인증을 성공할 수 있게 설계하였다.

---

## 2. 다회용컵 사용 인증

Yolov5 모델을 활용하여 [머그컵 이미지 데이터셋](https://universe.roboflow.com/house-vbmq0/mug-achwt)을 삽입하여 AI모델을 학습시킨다.

학습시킨 모델은 채식 인증과 같은 방식으로 인증을 확인한다.

---

## 3. 걸음 수 인증

안드로이드 스튜디오 내장 sensor를 사용하여 걸음 수를 측정하고, 측정한 걸음 수를 거리로 환산하여 절약한 탄소배출량을 계산해 업로드한다.

---

## 그외
### 홈 화면
: 현재 로그인한 계정의 이번 주 동안 인증한 데이터를 확인해 볼 수 있다. 절약한 탄소배출량에 따른 캐릭터와 그 캐릭터에 해당하는 설명이 같이 화면에 출력된다.

### 사용자 목록
: 이 애플리케이션을 사용하는 다른 사용자들의 닉네임과 이번 주 절약한 탄소배출량, 그리고 그에 해당하는 캐릭터들이 함께 리스트뷰에 출력된다. 이를 통해 사용자는 자신보다 많이 절약한 사용자를 보고 자극을 받거나, 곧 자신을 따라잡을 것 같은 사용자를 보고 의지를 다질 수 있다.

### 지난 주 정보
: 현재 로그인한 계정이 지난 주에 인증한 데이터를 바탕으로 계산된 절약한 탄소배출량을 확인할 수 있고, 이에 따른 캐릭터와 그에 해당하는 설명이 같이 화면에 출력된다.
