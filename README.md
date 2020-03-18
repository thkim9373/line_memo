# 간단한 설명
* 구조 : MVVM
  * Model
    * [Repository](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/repository/AppRepository.java), [Database](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/db/AppDatabase.java)
  * View
    * Activity : [MainActivity](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/main/MainActivity.java), 
[Gallery Activity](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/gallery/GalleryActivity.java)
    * Fragment : [List Fragment](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/main/fragments/list/ListFragment.java), [Viewer Fragment](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/main/fragments/viewer/ViewerFragment.java), [Editor Fragment](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/main/fragments/editor/EditorFragment.java)
  * View Model
    * [Main View Model](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/main/MainViewModel.java), [Gellary View Model](https://github.com/thkim9373/hoony_memo/blob/master/app/src/main/java/com/hoony/line_memo/gallery/GalleryViewModel.java)

* 사용한 외부 라이브러리 
  * [Glide](https://github.com/bumptech/glide), [Rounded Image View](https://github.com/vinc3m1/RoundedImageView)
