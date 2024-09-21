package vn.ducbao.springboot.webbansach_backend.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.ducbao.springboot.webbansach_backend.config.VnpayConfig;
import vn.ducbao.springboot.webbansach_backend.dto.request.VNPayRequest;
import vn.ducbao.springboot.webbansach_backend.dto.response.VNPAYResponse;
import vn.ducbao.springboot.webbansach_backend.entity.TranzacsitionInfo;
import vn.ducbao.springboot.webbansach_backend.repository.TranzacsitionInfoRepository;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VnpayController {
    private final TranzacsitionInfoRepository tranzacsitionInfoRepository;
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestParam("amount") double amountRequest)
            throws UnsupportedEncodingException {
        String orderType = "other";
        long amount = (long) (amountRequest * 100);
        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnpayConfig.getIpAddress(request);
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;

        return ResponseEntity.status(HttpStatus.OK).body(paymentUrl);
    }

    @PostMapping("/payment_info")
    public ResponseEntity<?> paymentSuccess(@RequestBody VNPayRequest vnPayRequest)
            throws UnsupportedEncodingException {
        // Extract parameters from VNPayRequest
        Map<String, String> params = new LinkedHashMap<>();
        params.put("vnp_Amount", vnPayRequest.getVnp_Amount());
        params.put("vnp_BankCode", vnPayRequest.getVnp_BankCode());
        params.put("vnp_BankTranNo", vnPayRequest.getVnp_BankTranNo());
        params.put("vnp_CardType", vnPayRequest.getVnp_CardType());
        params.put("vnp_OrderInfo", vnPayRequest.getVnp_OrderInfo());
        params.put("vnp_PayDate", vnPayRequest.getVnp_PayDate());
        params.put("vnp_ResponseCode", vnPayRequest.getVnp_ResponseCode());
        params.put("vnp_TmnCode", vnPayRequest.getVnp_TmnCode());
        params.put("vnp_TransactionNo", vnPayRequest.getVnp_TransactionNo());
        params.put("vnp_TransactionStatus", vnPayRequest.getVnp_TransactionStatus());
        params.put("vnp_TxnRef", vnPayRequest.getVnp_TxnRef());
        params.remove("vnp_SecureHash");
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName)
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()))
                        .append('&');
            }
        }

        // Remove the last '&' character
        if (hashData.length() > 0) {
            hashData.deleteCharAt(hashData.length() - 1);
        }

        String signValue = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());

        if (signValue.equals(vnPayRequest.getVnp_SecureHash())) {
            String vnp_ResponseCode = vnPayRequest.getVnp_ResponseCode();
            if ("00".equals(vnp_ResponseCode)) {
                TranzacsitionInfo tranzacsitionInfo = TranzacsitionInfo.builder()
                        .amount(Integer.parseInt(vnPayRequest.getVnp_Amount()))
                        .build();
                tranzacsitionInfoRepository.save(tranzacsitionInfo);
                return ResponseEntity.ok(
                        VNPAYResponse.builder().status("success").build());
            } else {
                return ResponseEntity.ok(
                        VNPAYResponse.builder().status("failed").build());
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
