package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {

		/**loginId,password欄にStudentAA03を入力 */
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA03");

		webDriver.findElement(By.id("password")).sendKeys("StudentAA03");

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "input");

		/** ログインボタンを押下 */
		webDriver.findElement(By.className("btn-primary")).click();

		/** 利用規約画面判定1*/
		String currentUrl = webDriver.getCurrentUrl();
		assertTrue("押下後の画面URLが一致しません（現在のURL: " + currentUrl + "）",
				currentUrl.contains("http://localhost:8080/lms/user/agreeSecurity"));

		/** 利用規約画面判定2*/
		String currentPageTitle = webDriver.findElement(By.cssSelector("h2")).getText();
		assertTrue("利用規約画面ではありません、または利用規約画面が正しく表示されていません", currentPageTitle.contains("利用規約"));

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {

		WebElement checkbox = webDriver.findElement(By
				.xpath("//form[contains(@action, 'agreeSecurity')]//input[@type='checkbox' and @name='securityFlg']"));
		if (!checkbox.isSelected()) {
			checkbox.click();
		}

		/** チェックを入れて次へボタンクリック*/
		webDriver.findElement(By.xpath("//form[contains(@action, 'agreeSecurity')]//button[text()='次へ']")).click();

		/** パスワード変更画面判定1*/
		String currentUrl = webDriver.getCurrentUrl();
		assertTrue("押下後の画面URLが一致しません（現在のURL: " + currentUrl + "）",
				currentUrl.contains("http://localhost:8080/lms/password/changePassword"));

		/** パスワード変更画面判定2*/
		String currentPageTitle = webDriver.findElement(By.cssSelector("h2")).getText();
		assertTrue("パスワード変更画面ではありません、またはパスワード変更画面が正しく表示されていません", currentPageTitle.contains("パスワード変更"));

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {

		webDriver.findElement(By.xpath("//form[@id='upd-form']//button[@type='submit' and text()='変更']")).click();

		visibilityTimeout(By.className("error"), 5);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		webDriver.findElement(By.id("upd-btn")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**エラーメッセージ判定*/
		String actualMessage = webDriver
				.findElement(By
						.xpath("//input[@id='currentPassword']/following-sibling::ul//span[contains(@class, 'error')]"))
				.getText();
		assertTrue(actualMessage.contains("現在のパスワードは必須です。"), "エラーメッセージが正しく表示されていません。実際の表示: " + actualMessage);

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {

		webDriver.navigate().refresh();

		String longPassword = "Abcdefg12345678901234567";

		webDriver.findElement(By.id("currentPassword")).sendKeys("StudentAA03");

		webDriver.findElement(By.id("password")).sendKeys(longPassword);

		webDriver.findElement(By.id("passwordConfirm")).sendKeys(longPassword);

		scrollBy("300");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		webDriver.findElement(By.xpath("//form[@id='upd-form']//button[@type='submit' and text()='変更']")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		webDriver.findElement(By.id("upd-btn")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**エラーメッセージ判定*/
		String actualMessage = webDriver
				.findElement(By.xpath("//input[@id='password']/following-sibling::ul//span[contains(@class, 'error')]"))
				.getText();
		assertTrue(actualMessage.contains("パスワードの長さが最大値(20)を超えています。"), "エラーメッセージが正しく表示されていません。実際の表示: " + actualMessage);

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {

		webDriver.navigate().refresh();

		String noPolicyPassword = "ｓｆｇｈｋｌＳＦＧＪＫＬ＋：＊＃＄％＆＠」」」」」」」？￥！！！！";

		webDriver.findElement(By.id("currentPassword")).clear();
		webDriver.findElement(By.id("currentPassword")).sendKeys("StudentAA03");

		webDriver.findElement(By.id("password")).clear();
		webDriver.findElement(By.id("password")).sendKeys(noPolicyPassword);

		webDriver.findElement(By.id("passwordConfirm")).clear();
		webDriver.findElement(By.id("passwordConfirm")).sendKeys(noPolicyPassword);

		scrollBy("300");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		webDriver.findElement(By.xpath("//form[@id='upd-form']//button[@type='submit' and text()='変更']")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		webDriver.findElement(By.id("upd-btn")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**エラーメッセージ判定*/
		String actualMessage = webDriver
				.findElement(By.xpath("//input[@id='password']/following-sibling::ul//span[contains(@class, 'error')]"))
				.getText();
		assertTrue(actualMessage.contains("「パスワード」には半角英数字のみ使用可能です。"), "エラーメッセージが正しく表示されていません。実際の表示: " + actualMessage);

		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() {

		webDriver.navigate().refresh();

		String Password = "Password01";

		String differentPassword = "Password02";

		webDriver.findElement(By.id("currentPassword")).clear();
		webDriver.findElement(By.id("currentPassword")).sendKeys("StudentAA03");

		webDriver.findElement(By.id("password")).clear();
		webDriver.findElement(By.id("password")).sendKeys(Password);

		webDriver.findElement(By.id("passwordConfirm")).clear();
		webDriver.findElement(By.id("passwordConfirm")).sendKeys(differentPassword);

		scrollBy("300");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		webDriver.findElement(By.xpath("//form[@id='upd-form']//button[@type='submit' and text()='変更']")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		webDriver.findElement(By.id("upd-btn")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		/**エラーメッセージ判定*/
		String actualMessage = webDriver
				.findElement(By.xpath("//input[@id='password']/following-sibling::ul//span[contains(@class, 'error')]"))
				.getText();
		assertTrue(actualMessage.contains("パスワードと確認パスワードが一致しません。"), "エラーメッセージが正しく表示されていません。実際の表示: " + actualMessage);

		
		/** エビデンスキャプチャ取得 */
		getEvidence(new Object() {
		}, "result");

	}

}
