
/**
 * PACKAGE NAME xyz.ryochin.qittaro.login
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/21
 */
package xyz.ryochin.qittaro.login;

public interface LoginView {

    public void showProgress();
    public void hideProgress();
    public void showMessage(String title, String message);
    public void navigateToHome();
}
