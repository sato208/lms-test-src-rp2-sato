package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		scrollBy("400");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/** 未提出詳細ボタンクリックtodo*/
		webDriver.findElement(By.xpath("//tr[contains(., '未提出')]//input[@value='詳細']")).click();

		/** セクション詳細画面判定1*/
		String expectedUrl = "http://localhost:8080/lms/section/detail";
		assertEquals(expectedUrl, webDriver.getCurrentUrl(), "ログイン成功後の画面URLが一致しません");

		/** セクション詳細画面判定2*/
		String currentPageTitle = webDriver.findElement(By.cssSelector(".breadcrumb li.active")).getText();
		assertEquals("セクション詳細", currentPageTitle, "セクション詳細画面ではありません、またはセクション詳細画面が正しく表示されていません");

		/** セクション詳細画面判定3*/
		String subTitle = webDriver.findElement(By.cssSelector("#sectionDetail h3")).getText();
		assertEquals("本日の内容", subTitle, "セクション詳細画面ではありません、またはセクション詳細画面が正しく表示されていません");

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		/** 「提出する」ボタンクリック*/
		webDriver.findElement(By.cssSelector(".table tr input[type='submit']")).click();

		/** レポート登録画面判定1*/
		String expectedUrl = "http://localhost:8080/lms/report/regist";
		assertEquals(expectedUrl, webDriver.getCurrentUrl(), "ログイン成功後の画面URLが一致しません");

		/** レポート登録画面判定2*/
		String currentPageTitle = webDriver.findElement(By.cssSelector("h2")).getText();
		assertTrue("レポート登録画面ではありません、またはレポート登録画面が正しく表示されていません", currentPageTitle.contains("日報【デモ】"));

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/** 報告内容入力*/
		webDriver.findElement(By.id("content_0")).sendKeys("ありがとうございました。");
		
		/** 提出するクリック*/
		webDriver.findElement(By.cssSelector("button.btn.btn-primary")).click();

		/** セクション詳細画面判定*/
		String currentUrl = webDriver.getCurrentUrl();
		assertTrue("提出する押下後の画面が一致しません（現在のURL: " + currentUrl + "）",
				currentUrl.contains("http://localhost:8080/lms/section/detail"));

		/** ボタン名判定*/
		String buttonTitle = webDriver.findElement(By.cssSelector(".table tr input[type='submit']"))
				.getAttribute("value");
		assertTrue("ボタン名が更新されていません", buttonTitle.contains("提出済み日報【デモ】を確認する"));

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

}
