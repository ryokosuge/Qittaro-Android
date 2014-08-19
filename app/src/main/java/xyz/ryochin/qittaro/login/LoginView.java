/**
 * =====================================
 * ENCODE : UTF-8
 * CREATED AT 2014/08/19
 * CREATED BY kosugeryou
 * =====================================
 */
package xyz.ryochin.qittaro.login;

public interface LoginView {

    public void showProgress();
    public void hideProgress();
    public void showMessage(String title, String message);
    public void navigateToHome();
}
