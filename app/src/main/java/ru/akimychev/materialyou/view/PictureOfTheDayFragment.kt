package ru.akimychev.materialyou.view

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import ru.akimychev.materialyou.viewmodel.PictureOfTheDayAppState
import ru.akimychev.materialyou.viewmodel.PictureOfTheDayViewModel
import ru.akimychev.materialyou.R
import ru.akimychev.materialyou.databinding.FragmentPictureOfTheDayBinding

class PictureOfTheDayFragment : Fragment() {
    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

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

    private fun renderData(data: PictureOfTheDayAppState) {
        when (data) {
            is PictureOfTheDayAppState.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
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