# 🏛️ LAW-IN-COMM: AI 어시스턴트 기반 법률 상담 커뮤니티 플랫폼

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.8-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-blue.svg)](https://www.docker.com/)

## 개요
### 시스템의 목표
LAW-IN-COMM은 법률 문제로 어려움을 겪는 일반 사용자가 AI 어시스턴트와 전문 변호사로부터 신속하고 정확한 도움을 받을 수 있는 종합 법률 상담 커뮤니티 플랫폼입니다.

복잡한 법률 용어나 절차, 그리고 비용 부담으로 인해 법률 서비스의 문턱을 높게 느끼는 사용자들에게 누구나 쉽게 접근할 수 있는 환경을 제공하고자 하였습니다. 본 시스템은 기존 법률 상담의 한계를 넘어 기술을 통해 사람과 사람, 사람과 법률을 연결하는 것을 목표로 합니다.

저희는 아래의 핵심 가치를 사용자에게 제공하고자 노력했습니다.

- **AI를 통한 1차 상담**: RAG 기술을 활용하여 판례 기반의 신뢰도 높은 답변을 빠르게 제공합니다.
- **전문가와의 연결**: 검증된 변호사와 실시간으로 소통하며 심도 있는 상담을 받을 수 있습니다.
- **지식 공유 커뮤니티**: 법률적 고민을 나누고 해결책을 공유하는 건강한 커뮤니티를 형성합니다.

## 프로젝트 구조
![시스템 아키텍처](https://github.com/user-attachments/assets/47b2a635-64d4-4ec2-9fcf-9550304fc34f)<br>
캡스톤 디자인 프로젝트의 특성상 신속한 프로토타이핑과 개발 효율성을 고려하여 Spring Boot 기반의 모놀리식 아키텍처로 설계했습니다. 다만, AI 모델 서빙은 별도의 FastAPI 서버로 분리하여 각 서비스의 특성에 맞는 유연한 운영이 가능하도록 구성했습니다. 또한 Docker를 활용하여 온프레미스 환경에서도 일관된 배포가 가능하도록 인프라를 구축했습니다.

## 기여 내용

### 법률 특화 임베딩 모델 개발
법률 상담 서비스의 핵심은 사용자의 질문 의도를 정확히 파악하고, 수많은 판례 중에서 가장 적합한 사례를 찾아주는 것입니다. 하지만 일반적인 범용 임베딩 모델은 '선의', '악의'와 같은 법률 전문 용어의 특수한 맥락을 제대로 이해하지 못해 검색 품질이 떨어지는 문제가 있었습니다.

이를 해결하기 위해 저는 법률 도메인에 특화된 임베딩 모델을 직접 개발하기로 결정했습니다.

#### 데이터셋 구축 및 Fine-tuning
AI Hub에서 제공하는 양질의 '법률 규정(판결문) 연계 질의응답 데이터셋'을 활용하였습니다. 한국어 처리에 강점이 있는 사전 학습 모델(KLUE-RoBERTa 등)을 베이스로 선정하고, **Contrastive Learning(대조 학습)** 기법을 적용하여 모델이 법률 문장 간의 유사도를 더 정교하게 학습하도록 미세 조정(Fine-tuning)을 수행했습니다.

이 과정을 통해 Base Model 대비 검색 정확도 지표인 **NDCG@5 Score를 약 7.2% 향상**시키는 성과를 거두었습니다. 이는 RAG(검색 증강 생성) 시스템의 전반적인 답변 품질을 높이는 결정적인 역할을 했습니다.

#### Gradient Caching을 통한 배치 사이즈 한계 극복
![성능 비교](https://github.com/user-attachments/assets/f911a5f3-88c9-4ff6-9746-c85b0b5cb161)<br>
대조 학습의 성능을 극대화하기 위해서는 배치 사이즈(Batch Size)를 키워 한 번에 많은 Negative Sample을 학습하는 것이 중요합니다. 하지만 제가 사용할 수 있는 자원은 단일 GPU(VRAM 24GB)였기 때문에, 물리적으로 큰 배치 사이즈를 올리는 데 한계가 있었습니다. OOM이 빈번하게 발생했고, 이는 학습 성능을 저하시키는 원인이 되었습니다.

이러한 하드웨어의 제약을 극복하기 위해 **Gradient Caching** 기법을 도입했습니다. 이는 그래디언트 계산을 분할하여 수행함으로써 메모리 사용량을 획기적으로 줄이는 방법입니다. 이를 통해 물리적인 메모리 한계를 넘어 **논리적으로 배치 사이즈를 512까지 확장**할 수 있었고, 안정적인 학습 환경에서 모델 성능을 최대한으로 끌어올릴 수 있었습니다.

### 유연하고 확장성 있는 DDD 기반 아키텍처 설계
단순히 기능만 동작하는 코드가 아니라, 유지보수하기 쉽고 비즈니스 요구사항의 변화에 유연하게 대응할 수 있는 시스템을 만들고 싶었습니다. 이를 위해 도메인 주도 설계(DDD)의 사상을 반영하고 다양한 디자인 패턴을 적재적소에 활용했습니다.

#### JPA 상속 전략을 통한 사용자 모델링
'일반 사용자'와 '변호사'는 '회원'이라는 공통점이 있지만, 다루는 정보와 시스템 내에서의 역할은 명확히 다릅니다. 이 둘을 별개의 테이블로 관리하면 중복이 발생하고, 하나의 테이블로 합치면 불필요한 null 컬럼이 많아지는 문제가 있었습니다.

저는 이 문제를 해결하기 위해 JPA의 **InheritanceType.JOINED** 전략을 사용했습니다. 공통 속성은 부모 테이블에, 각자의 고유 속성은 자식 테이블에 정규화하여 저장함으로써 데이터 무결성을 확보했습니다. 또한 코드 레벨에서는 **Template Method 패턴**을 적용하여, '이름 반환'과 같은 공통 로직의 큰 틀은 상위 클래스에 정의하고, 구체적인 동작(익명 처리 vs 실명 반환)은 하위 클래스에서 구현하도록 하여 코드의 중복을 줄이고 응집도를 높였습니다.

#### 전략(Strategy) 및 팩토리(Factory) 패턴 적용
사용자 프로필 이미지, 변호사 자격증 등 시스템에서 다루는 파일의 종류는 다양했고, 추후 저장소가 AWS S3에서 변경될 가능성도 있었습니다.
이에 **Factory 패턴**을 도입하여 파일 타입(ImageType)에 따라 적절한 파일 처리 객체를 생성하도록 캡슐화했습니다. 또한 **Strategy 패턴**을 활용하여 파일 저장 로직을 인터페이스로 추상화함으로써, 비즈니스 로직의 수정 없이 저장소 구현체만 갈아끼우면 되는 유연한 구조를 완성했습니다.

### 시스템 성능 및 보안 최적화

#### 비동기 처리를 통한 AI 응답 지연 해결
고도화된 AI 답변을 생성하는 RAG 프로세스는 필연적으로 수 초 이상의 시간이 소요됩니다. 사용자가 질문을 올리고 화면이 멈춘 채로 기다리게 하는 것은 사용자 경험(UX) 측면에서 좋지 않다고 판단했습니다.

이를 개선하기 위해 Spring의 **`@Async`** 어노테이션을 활용하여 AI 추론 요청을 비동기로 처리하도록 구현했습니다. 서버는 사용자의 질문 등록 요청에 대해 "등록되었습니다"라는 응답을 즉시 반환하고, 백그라운드 스레드에서 AI 작업을 수행합니다. 작업이 완료되면 별도의 로직을 통해 결과를 업데이트하는 방식으로 변경하여, 사용자가 느끼는 체감 대기 시간을 획기적으로 줄였습니다.

#### 인터셉터를 활용한 WebSocket 보안 강화
실시간 채팅 기능은 민감한 법률 상담이 오가는 공간이므로 철저한 보안이 필수적입니다. HTTP 요청과 달리 지속적인 연결을 유지하는 WebSocket 통신에서도 확실한 인증 체계가 필요했습니다.

저는 STOMP 프로토콜의 연결(CONNECT) 단계에서 보안을 강화하기 위해 **`ChannelInterceptor`**를 구현했습니다. 메시지가 브로커에 도달하기 전인 인터셉터 단계에서 JWT 토큰의 유효성을 검증하도록 하여, 비인가 사용자의 접근을 원천적으로 차단했습니다. 이를 통해 별도의 복잡한 보안 로직 없이도 안전하고 신뢰할 수 있는 채팅 환경을 구축할 수 있었습니다.

## 화면 시안

### 메인 화면 및 로그인
<img width="1256" src="https://github.com/user-attachments/assets/9bb3f45e-5a5e-4d16-b6d4-3d34196a8fcc" />
<img width="1272" src="https://github.com/user-attachments/assets/056ccddc-0990-42ee-863a-c05085147f0a" />

### 회원가입 (변호사)
<img width="1256" src="https://github.com/user-attachments/assets/09aff84d-bbef-4b79-b748-6746ee4466cc" />

### AI 법률 상담
<img width="761" src="https://github.com/user-attachments/assets/485a3d8e-40ee-4993-becb-8f3ea2e76652" />

### 실시간 채팅 및 질문 게시판
<img width="378" src="https://github.com/user-attachments/assets/43d27eb9-ca21-4b7f-8893-bd7eb359ee57" />
<img width="1256" src="https://github.com/user-attachments/assets/bc3011df-4eda-4471-8a04-054494b186e5" />

---

### 관련 링크
* **GitHub Repository**: [capstone2_BE_WAS](https://github.com/kit-se-capstone2/capstone2_BE_WAS)
* **AI Model (Hugging Face)**: [KURE-legal-ft-v1](https://huggingface.co/kakao1513/KURE-legal-ft-v1)
* **API Documentation**: Swagger UI (애플리케이션 실행 후 `/api-docs`)
