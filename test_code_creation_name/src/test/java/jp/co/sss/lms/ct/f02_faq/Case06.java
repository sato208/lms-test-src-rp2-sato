package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {

		/** URL取得 */
		String targetUrl = "http://localhost:8080/lms/";
		/** URL開く */
		goTo(targetUrl);
		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		});
		/** ログイン画面の判定1 */
		String actualText = "ログイン";
		String headingText = webDriver.findElement(By.tagName("h2")).getText();
		assertEquals(actualText, headingText, "ログイン画面ではありません、またはログイン画面が正しく表示されていません");

		/** ログイン画面の判定2 */
		String actualButtonText = webDriver.findElement(By.className("btn-primary")).getAttribute("value");
		assertEquals(actualText, actualButtonText, "ログイン画面ではありません、またはログイン画面が正しく表示されていません");

	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		/**loginId,password欄にStudentAA01を入力 */
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");

		webDriver.findElement(By.id("password")).sendKeys("StudentAA01");

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "input");
		/** ログインボタンを押下 */
		webDriver.findElement(By.className("btn-primary")).click();

		/** コース詳細画面判定1*/
		String expectedUrl = "http://localhost:8080/lms/course/detail";
		assertEquals(expectedUrl, webDriver.getCurrentUrl(), "ログイン成功後の画面URLが一致しません");

		/** コース詳細画面判定2*/
		String actualBreadcrumb = webDriver.findElement(By.className("breadcrumb")).findElement(By.tagName("li"))
				.getText();
		assertEquals("コース詳細", actualBreadcrumb, "コース詳細画面ではありません、またはコース詳細画面が正しく表示されていません");

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		/** 上部メニュー機能を開く */
		webDriver.findElement(By.className("dropdown-toggle")).click();
		//		webDriver.findElement(By.linkText("機能")).click();

		/** メニューが開いたら、中にある「ヘルプ」クリック*/
		webDriver.findElement(By.xpath("//a[@href='/lms/help']")).click();
		//		webDriver.findElement(By.className("dropdown-menu")).findElement(By.linkText("ヘルプ")).click();

		/** ヘルプ画面判定1*/
		String expectedHelpUrl = "http://localhost:8080/lms/help";
		assertEquals(expectedHelpUrl, webDriver.getCurrentUrl(), "ヘルプ画面のURLが一致しません");

		/** ヘルプ画面判定2*/
		String actualHelpTitle = webDriver.findElement(By.tagName("h2")).getText();
		assertEquals("ヘルプ", actualHelpTitle, "ヘルプ画面のタイトルが正しく表示されていません、またはヘルプ画面が正しく表示されていません");

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		// 1. 現在のタブのIDをメモしておく
		String oldWindow = webDriver.getWindowHandle();

		// 2. 「よくある質問」リンク文字をクリック（別タブが開く）
		webDriver.findElement(By.linkText("よくある質問")).click();

		// 3. 今開いてるタブとは違うタブを開く
		for (String windowHandle : webDriver.getWindowHandles()) {
			if (!oldWindow.contentEquals(windowHandle)) {
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}

		/** よくある質問画面判定1*/
		String expectedFaqUrl = "http://localhost:8080/lms/faq";
		assertEquals(expectedFaqUrl, webDriver.getCurrentUrl(), "よくある質問画面のURLが一致しません");

		/** よくある質問画面判定2*/
		String actualFaqTitle = webDriver.findElement(By.tagName("h2")).getText();
		assertEquals("よくある質問", actualFaqTitle, "よくある質問画面ではありません、またはよくある質問画面が正しく表示されていません");

		/** エビデンスキャプチャ（結果）取得 */
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		
		/**タブ切り替え（エラー対策） */
		for (String windowHandle : webDriver.getWindowHandles()) {
			webDriver.switchTo().window(windowHandle);
			if (webDriver.getCurrentUrl().contains("/lms/faq")) {
				break; 
			}
		}
		
		/**カテゴリ検索*/
		webDriver.findElement(By.linkText("【研修関係】")).click();
		
		/**検索結果の判定1（件数）*/
		int actualCount = webDriver.findElements(By.cssSelector(".table tbody tr")).size();
		assertEquals(2, actualCount, "検索結果のヒット件数が一致しません");
		
		/**検索結果の判定2（中身1）*/
		WebElement firstQuestion = webDriver.findElement(By.cssSelector(".table tbody tr:nth-child(1) dl"));
		String firstQuestionText = firstQuestion.getText();
		assertTrue(firstQuestionText.contains("キャンセル"), "キーワード『キャンセル』が含まれていません");
		
		/**検索結果の判定2（中身2）*/
		WebElement secondQuestion = webDriver.findElement(By.cssSelector(".table tbody tr:nth-child(2) dl"));
		String secondQuestionText = secondQuestion.getText();
		assertTrue(secondQuestionText.contains("研修"), "2件目の検索結果に『研修』が含まれていません");
		
		/**下までスクロール（確実に撮るため待機時間の確保）*/
		try {
	        Thread.sleep(3000); 
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
		
		scrollTo("document.body.scrollHeight");
		
		try {
	        Thread.sleep(3000); 
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
		
		/** エビデンスキャプチャ（結果）取得 */
		getEvidence(new Object() {
		});
		
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		
		/**タブ切り替え（エラー対策） */
		for (String windowHandle : webDriver.getWindowHandles()) {
			webDriver.switchTo().window(windowHandle);
			if (webDriver.getCurrentUrl().contains("/lms/faq")) {
				break;
			}
		}
		
		/** 検索結果の質問クリック*/
		WebElement firstQuestionDl = webDriver.findElement(By.cssSelector(".table tbody tr:nth-child(1) dl"));
		firstQuestionDl.click();
		
		/** 回答表示の判定*/
		WebElement firstAnswerDd = webDriver.findElement(By.cssSelector(".table tbody tr:nth-child(1) dd"));
		assertTrue(firstAnswerDd.isDisplayed(), "回答が表示されていません");
		
		/** エビデンスキャプチャ（結果）取得 */
		getEvidence(new Object() {
		});
	}

}
