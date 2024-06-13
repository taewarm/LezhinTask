## Kotlin Coroutine Flow관련
---
### Cold Flow 와 Hot Flow 에 대해 자세히 설명해 주세요.
cold flow는 1:1 소통이며 받는사람이 한명 데이터를 요청하면 flow를 만들어 발행해줍니다. collect를 실행할때마다 동작하며 보통 데이터베이스를 읽어오거나 서버에서 값을 읽어오는경우를 ColdFlow로 작업합니다.
hot flow는 1:N 소통이며 받는사람이 없어도 데이터를 발행합니다. 보통 변화가 많은 상태값을 읽어올때 사용합니다 값을 observe하는 느낌으로 사용하며 stateFlow나 SharedFlow가 적절한 예시로 사용됩니다.
### StateFlow 와 SharedFlow 에 대해 자세히 설명해 주세요.
StateFlow는 상태를 저장하고 항상 최신 상태를 유지하며 구독했을경우 상태값을 최신상태로 전달합니다. 초기값이 필요하며 상태값 을 항상 가지고 있습니다. collect를 했을경우 최신값을 전달받게 됩니다. 
SharedFlow는 상태를 저장하지않고 초기값을 필요로 하지않습니다. 새로운 데이터가 발생하면 버퍼에 저장되고 나중에 수신할수있도록 대기상태로 있습니다. 그러면 구독한 사람들은 동시에 같은 이벤트를 받을수 있습니다. replay를 통해 여러개의 이벤트를 재생성할수도 있습니다
stateFlow와 SharedFlow에 대해 비교가되는것이 LiveData며 LiveData와 달리 생명주기에 영향을 받지 않아 생명주기에 맞춰 사용하려면 repeatonlifecycle 을 사용하여 생명주기를 맞춰줘야한다.
## Android ViewModel 관련
---
### owner에 대해 자세히 설명해 주세요.
owner는 ViewModel의 생명주기를 관리하기위해 필요한 컴포넌트입니다. 보통 Activity나 Fragment에서 ViewModel의 생명주기를 맞추기 위해 사용됩니다. 잘못관리했을경우 ViewModel의 데이터가 날아가거나 함수를 사용할수 없게됩니다.
### Compose Navigation 과 Dagger Hilt를 같이 사용하는 경우, hiltViewModel() 로 ViewModel instance를 가져올 때 owner를 어떻게 설정해야 하는지 자세히 설명해 주세요.
이번에 개발한 과제의 상황으로 본다면 Activity한개에 composable함수를 올린모습으로 제 기준으로는 검색화면과 북마크 화면을 Fragment로 생각하며 개발하였습니다. 그 화면내에 생명주기에 맞춘다면 다른화면을 나갔다 온경우 가지고있던 데이터가 손실이 나는경우가 생겨 Activity생명주기에 맞춰 개발하였고
```
viewModel: BookMarkViewModel = hiltViewModel(LocalContext.current as MainActivity)
```
이런식으로 viewmodel를 가져와 데이터 손실없이 작업하였습니다.
## Android Paging3 관련
---
### PagingSource 와 getRefreshKey 함수의 파라미터와 리턴값에 대해 자세히 설명해 주세요.
함수의 파라미터는 PagingState객체로 주로 pages 와 anchorPosition, config 변수를 사용합니다.
page의 경우 load된 list의 데이터를 가져와 사용할수있으며,
anchorPosition은 가장 최근에 접근한 index의 값을 가지고있습니다.  anchorPosition이 null인 경우는 PageData를 가져오지 못한경우에 null이 됩니다.
config는 Pager객체 생성을 할때 선언해줘야하는 파라미터중 config에 넣어준 값을 가져와 사용할수있는 변수

### PagingSource 의 load 함수의 파라미터와 리턴 값에 대해 자세히 설명해 주세요.
파라미터에는 LoadParams객체로 사용되는 변수에는 첫번쨰로 loadSize 로드 할 항목 개수를 가지고있는변수이다 두번째는 placeholdersEnabled로 데이터가 로드 되지않았을때 아래에 빈자리로 표시되었을때 true값을 가집니다. false일 경우에는 아무것도 표시되지않습니다. 사용할때는 Pager객체 생성시 config부분에 PagingConfig객체를 생성후 enablePlaceholders 파라미터에 값을 넣어주면 된다 