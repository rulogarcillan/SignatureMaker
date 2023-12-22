package com.signaturemaker.app.application.features.menu

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import com.signaturemaker.app.R
import com.signaturemaker.app.application.core.extensions.getNavOptions
import com.signaturemaker.app.application.core.extensions.openRate
import com.signaturemaker.app.application.core.platform.GlobalFragment
import com.signaturemaker.app.application.features.menu.MenuIdentifier.CHANGELOG
import com.signaturemaker.app.application.features.menu.MenuIdentifier.GDPR
import com.signaturemaker.app.application.features.menu.MenuIdentifier.LICENSE
import com.signaturemaker.app.application.features.menu.MenuIdentifier.MOREAPP
import com.signaturemaker.app.application.features.menu.MenuIdentifier.PRIVACY
import com.signaturemaker.app.application.features.menu.MenuIdentifier.RATE
import com.signaturemaker.app.application.features.menu.MenuIdentifier.SETTING
import com.signaturemaker.app.application.features.menu.MenuIdentifier.SUGGEST
import com.signaturemaker.app.application.features.suggest.CustomDialogSuggest
import com.signaturemaker.app.databinding.SettingFragmentBinding
import com.tuppersoft.skizo.android.core.extension.getColorFromAttr
import com.tuppersoft.skizo.android.core.extension.loge
import java.util.Locale


class SettingFragment : GlobalFragment() {


    // Show a privacy options button if required.
    private val isPrivacyOptionsRequired: Boolean
        get() =
            (activity as? SettingActivity)?.let {
                it.consentInformation.privacyOptionsRequirementStatus ==
                        ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
            } ?: false


    override val toolbarTitle: String
        get() = ""
    override val showBackButton: Boolean
        get() = false

    companion object {

        const val urlPrivacy = "https://info.tuppersoft.com/privacy/privacy_policy_signature.html"
        const val userLinkedin = "raul-rodriguez-concepcion"
        const val urlLinkedin = "https://www.linkedin.com/in/$userLinkedin/"
        const val urlGithub = "https://github.com/rulogarcillan"
        const val userTwitter = "tuppersoft"
        const val urlRate = "market://details?id=com.signaturemaker.app"
        const val urlMoreApps = "market://search?q=pub:Raúl R."
    }

    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        context?.let {
            binding.tvVersion.text =
                "v${it.packageManager.getPackageInfo(it.packageName, 0).versionName}"
        }

        binding.ivLinkedin.setOnClickListener {
            openLinkedInPage()
        }
        binding.ivGithub.setOnClickListener { openGithub() }
        binding.ivTwitter.setOnClickListener { openTwitter() }
        binding.ivTelegram.setOnClickListener { openTelegram() }
    }

    private fun initRecyclerView() {

        val list: MutableList<ItemSettingMenu> = mutableListOf()

        list.add(ItemSettingMenu(SETTING, R.drawable.ic_settings, R.string.title_setting))
        list.add(ItemSettingMenu(SUGGEST, R.drawable.ic_message, R.string.send_suggestions))
        list.add(ItemSettingMenu(CHANGELOG, R.drawable.ic_changelog, R.string.changelog))
        list.add(ItemSettingMenu(RATE, R.drawable.ic_rate, R.string.rate))
        list.add(ItemSettingMenu(MOREAPP, R.drawable.ic_moreapp, R.string.more_app))
        list.add(ItemSettingMenu(LICENSE, R.drawable.ic_copyright, R.string.license))
        list.add(ItemSettingMenu(PRIVACY, R.drawable.ic_privacy, R.string.privacy_policy))
        list.add(ItemSettingMenu(GDPR, R.drawable.ic_action_list, R.string.handle_privacy_policy))


        val settingAdapter = SettingAdapter()
        binding.rvMenu.layoutManager = GridLayoutManager(context, 1)
        binding.rvMenu.adapter = settingAdapter
        settingAdapter.submitList(list)

        settingAdapter.setOnClickItemListener {
            handleClickMenu(it.id)
        }
    }

    private fun handleClickMenu(id: MenuIdentifier) {
        when (id) {
            LICENSE -> {
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.license))
                startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            }

            RATE -> showRateApp()
            MOREAPP -> openMoreApps()
            PRIVACY -> openPrivacy()
            CHANGELOG -> findNavController().navigate(R.id.ChangelogFragment, null, getNavOptions())
            SETTING -> findNavController().navigate(R.id.SettingsFragment, null, getNavOptions())
            SUGGEST -> showSendSuggest()
            GDPR -> {
                if (isPrivacyOptionsRequired) {
                    UserMessagingPlatform.showPrivacyOptionsForm(requireActivity()) { formError ->
                        formError?.message?.loge()
                    }
                }
            }
        }
    }

    private fun showRateApp() {

        context?.openRate()
        //Auto rate
        /*    val manager = ReviewManagerFactory.create(requireActivity())
            val request: Task<ReviewInfo> = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Getting the ReviewInfo object
                    val reviewInfo: ReviewInfo = task.result
                    val flow: Task<Void> = manager.launchReviewFlow(requireActivity(), reviewInfo)
                    flow.addOnCompleteListener { task1 ->
                        task.isComplete

                    }
                }
            }*/
    }

    private fun showSendSuggest() {
        val ft: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        ft.let {
            val dialog: CustomDialogSuggest = CustomDialogSuggest.getInstance()
            dialog.show(ft, "dialog")
            dialog.isCancelable = true
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun openPrivacy() {
        val builder = CustomTabsIntent.Builder()
        context?.let {
            val customTabsIntent = builder.setToolbarColor(it.getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
                .setNavigationBarColor(it.getColorFromAttr(androidx.appcompat.R.attr.colorPrimary)).build()
            customTabsIntent.intent.flags =
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or Intent.FLAG_ACTIVITY_NO_HISTORY
            customTabsIntent.launchUrl(it, Uri.parse(urlPrivacy))
        }
    }

    private fun openGithub() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlGithub))
        startActivity(intent)
    }

    /**
     * Start activity my others play store apps
     */
    private fun openMoreApps() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlMoreApps))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    private fun openTwitter() {
        var intent: Intent?
        try {

            activity?.packageManager?.getPackageInfo("com.twitter.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=$userTwitter"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: Exception) {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/$userTwitter"))
        }
        activity?.startActivity(intent)
    }

    private fun openTelegram() {
        val url = if (Locale.getDefault().language.toLowerCase(Locale.ROOT) == "es") {
            "https://telegram.me/signature_maker_es"
        } else {
            "https://telegram.me/signature_maker_eng"
        }

        val telegram = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(telegram)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cancel, menu)

        menu.findItem(R.id.idCancel).setOnMenuItemClickListener {
            activity?.let {
                it.finish()
                it.overridePendingTransition(R.anim.slide_from_top, R.anim.slide_in_top)
            }
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun openLinkedInPage() {
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://$userLinkedin"))
        val packageManager = context?.packageManager
        val list = packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list?.isEmpty() == true) {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlLinkedin))
        }
        startActivity(intent)
    }
}
