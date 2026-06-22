package jp.co.sss.lms.ct.f01_login1;

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

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		/**loginId,password欄にTestを入力 */
		webDriver.findElement(By.id("loginId")).sendKeys("Test");

		webDriver.findElement(By.id("password")).sendKeys("Test");

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "input");
		/** ログインボタンを押下 */
		webDriver.findElement(By.className("btn-primary")).click();

		/**エラーメッセージ判定*/
		String expectedMessage = "* ログインに失敗しました。";
		String actualMessage = webDriver.findElement(By.className("error")).getText();

		assertEquals(expectedMessage, actualMessage, "エラーメッセージが正しく表示されていません");

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

}
