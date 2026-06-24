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
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		/** 提出済み詳細ボタンクリック*/
		webDriver.findElement(By.xpath("//tr[contains(., '提出済み')]//input[@value='詳細']")).click();

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
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		/** 「確認する」ボタンクリック*/
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
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
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

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/** セクション詳細画面判定*/
		String currentUrl = webDriver.getCurrentUrl();
		assertTrue("押下後の画面URLが一致しません（現在のURL: " + currentUrl + "）",
				currentUrl.contains("http://localhost:8080/lms/section/detail"));

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/** 上部メニュー「ようこそ〇〇さん」クリック*/
		webDriver.findElement(By.cssSelector(".navbar-right small")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/** ユーザー詳細画面判定*/
		String currentUrl = webDriver.getCurrentUrl();
		assertTrue("押下後の画面URLが一致しません（現在のURL: " + currentUrl + "）",
				currentUrl.contains("http://localhost:8080/lms/user/detail"));

		/** レポート登録画面判定2*/
		String currentPageTitle = webDriver.findElement(By.cssSelector("h2")).getText();
		assertTrue("レポート登録画面ではありません、またはレポート登録画面が正しく表示されていません", currentPageTitle.contains("ユーザー詳細"));

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {

		scrollBy("800");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/** 当該レポートの「詳細」をクリック*/
		webDriver.findElement(By.xpath("//tr[contains(., '2022年10月1日(土)')]//input[@value='詳細']")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/** レポート詳細画面判定1*/
		String currentUrl = webDriver.getCurrentUrl();
		assertTrue("押下後の画面URLが一致しません（現在のURL: " + currentUrl + "）",
				currentUrl.contains("http://localhost:8080/lms/report/detail"));

		/** レポート詳細画面判定2*/
		String buttonTitle = webDriver.findElement(By.xpath("//button[text()='フィードバックコメントを登録する']"))
				.getText();
		assertTrue("レポート詳細画面ではありません、またはレポート詳細画面が正しく表示されていません", buttonTitle.contains("フィードバックコメントを登録する"));

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

}
