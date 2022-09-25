package ru.akimychev.materialyou.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import ru.akimychev.materialyou.R
import ru.akimychev.materialyou.databinding.FragmentPictureOfTheDayBinding
import ru.akimychev.materialyou.viewmodel.PictureOfTheDayAppState
import ru.akimychev.materialyou.viewmodel.PictureOfTheDayAppState.Success
import ru.akimychev.materialyou.viewmodel.PictureOfTheDayViewModel

class PictureOfTheDayFragment : Fragment() {
    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getData()
            .observe(viewLifecycleOwner) { renderData(it) }
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
    }

    private fun renderData(data: PictureOfTheDayAppState) {
        when (data) {
            is Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                val explanation = serverResponseData.explanation
                val title = serverResponseData.title
                if (url.isNullOrEmpty() && explanation.isNullOrEmpty() && title.isNullOrEmpty()) {
//TODO: Отобразите ошибку; showError("Сообщение, что ссылка пустая")
                    toast("Link is empty")
                } else {
//TODO: Отобразите фото; showSuccess()
//Coil в работе: достаточно вызвать у нашего ImageView нужную extension-функцию и передать ссылку на изображение
//а в лямбде указать дополнительные параметры (не обязательно) для отображения ошибки, процесса загрузки, анимации смены изображений
                    binding.imageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                        crossfade(true)
                    }
                    view?.findViewById<TextView>(R.id.bottomSheetDescription)?.text = explanation
                    view?.findViewById<TextView>(R.id.bottomSheetDescriptionHeader)?.text = title
                    binding.chip.setOnClickListener {
                        val HDUrl = serverResponseData.hdurl
                        binding.imageView.load(HDUrl)
                        view?.findViewById<Chip>(R.id.chip)?.text = "Updated"

                        view?.findViewById<TextView>(R.id.bottomSheetDescriptionHeader)?.text =
                            "$title(HD)"
                    }
                }
            }
            is PictureOfTheDayAppState.Loading -> {
//TODO: Отобразите загрузку; showLoading()
            }
            is PictureOfTheDayAppState.Error -> {
//TODO: Отобразите ошибку; showError(data.error.message)
                toast(data.error.message)
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}