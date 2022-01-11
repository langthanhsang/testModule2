package Manager;

import Model.Contact;
import Validate.Validate;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ContactManager {
    private final ArrayList<Contact> contactList;
    private final Scanner scanner = new Scanner(System.in);
    private final Validate validate = new Validate();
    public static final String PATH_NAME = "/Volumes/WorkSpace/Codegym/thithuchanh2/src/data/Contact";

    public ContactManager() {
        if (new File(PATH_NAME).length() == 0) {
            this.contactList = new ArrayList<>();
        } else {
            this.contactList = readFile(PATH_NAME);
        }
    }

    public ArrayList<Contact> getContactList() {
        return contactList;
    }

    public void addContact() {
        System.out.println("Mời bạn nhập thông tin:");
        System.out.println("--------------------");
        String phoneNumber = getPhoneNumber();
        System.out.println(" Nhập tên nhóm:");
        String group = scanner.nextLine();
        System.out.println(" Nhập Họ tên:");
        String name = scanner.nextLine();
        System.out.println(" Nhập giới tính:");
        String gender = scanner.nextLine();
        System.out.println(" Nhập địa chỉ:");
        String address = scanner.nextLine();
        System.out.println(" Nhập ngày sinh(dd-mm-yyyy):");
        String date = scanner.nextLine();
        LocalDate dateOfBirth = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-LL-yyyy"));
        String email = getEmail();

        for (Contact phone : contactList) {
            if (phone.getPhoneNumber().equals(phoneNumber)) {
                System.out.println(" Số điện thoại bị trùng, mời kiểm tra lại !");
                return;
            }
        }
        Contact contact = new Contact(phoneNumber, group, name, gender, address, dateOfBirth, email);
        contactList.add(contact);
        System.out.println("Thêm vào danh bạ thành công !");
    }

    public void updateContact(String phoneNumber) {
        Contact editContact = null;
        for (Contact contact : contactList) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                editContact = contact;
            }
        }
        if (editContact != null) {
            int index = contactList.indexOf(editContact);
            System.out.println(" Nhập tên nhóm mới:");
            editContact.setGroup(scanner.nextLine());
            System.out.println(" Nhập Họ tên mới:");
            editContact.setName(scanner.nextLine());
            System.out.println(" Nhập giới tính mới:");
            editContact.setGender(scanner.nextLine());
            System.out.println(" Nhập địa chỉ mới:");
            editContact.setAddress(scanner.nextLine());
            System.out.println(" Nhập ngày sinh mới(dd-mm-yyyy):");
            String date = scanner.nextLine();
            LocalDate dateOfBirth = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-LL-yyyy"));
            editContact.setDateOfBirth(dateOfBirth);
            editContact.setEmail(getEmail());
            contactList.set(index, editContact);
            System.out.println(" Sửa " + phoneNumber + " thành công !");
        } else {
            System.out.println(" Không tìm được số điện thoại trên !");
        }
    }

    public void deleteContact(String phoneNumber) {
        Contact deleteContact = null;
        for (Contact contact : contactList) {
            if (contact.getPhoneNumber().equals(phoneNumber)) {
                deleteContact = contact;
            }
        }
        if (deleteContact != null) {
            System.out.println(" Nhập xác nhận:");
            System.out.println(" 1. xoá");
            System.out.println(" 2. thoát");
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    contactList.remove(deleteContact);
                    System.out.println(" Xóa thành công !");
                    break;
                case 2:
                    System.exit(2);
                    break;
            }
        } else {
            System.out.println(" Không tìm thấy danh bạ với số điện thoại trên !");
        }
    }

    public void searchContactByNameOrPhone(String keyword) {
        ArrayList<Contact> contacts = new ArrayList<>();
        for (Contact contact : contactList) {
            if (validate.validatePhoneOrName(keyword, contact.getPhoneNumber()) || validate.validatePhoneOrName(keyword, contact.getName())) {
                contacts.add(contact);
            }
        }
        if (contacts.isEmpty()) {
            System.out.println(" Không tìm thấy danh bạ với từ khóa trên !");
        } else {
            System.out.println("Danh bạ cần tìm:");
            contacts.forEach(System.out::println);
        }
    }

    public void displayAll() {
        System.out.printf(" %-15s %-10s %-15s %-10s %-10s\n", "Số điện thoại", "Nhóm", "Họ tên", "Giới tính", "Địa chỉ");
        for (Contact contact : contactList) {
            System.out.printf(" %-15s %-10s %-15s %-10s %-10s\n", contact.getPhoneNumber(), contact.getGroup(), contact.getName(), contact.getGender(), contact.getAddress());
        }
    }

    public String getPhoneNumber() {
        String phoneNumber;
        while (true) {
            System.out.print("Nhập số điện thoại: ");
            String phone = scanner.nextLine();
            if (!validate.validatePhone(phone)) {
                System.out.println(" Số điện thoại không hợp lệ!");
                System.out.println("Số điện thoại phải có 9 số(0 - 9)");
            } else {
                phoneNumber = phone;
                break;
            }
        }
        return phoneNumber;
    }

    private String getEmail() {
        String email;
        while (true) {
            System.out.print("Nhập email: ");
            String inputEmail = scanner.nextLine();
            if (!validate.validateEmail(inputEmail)) {
                System.out.println(" Email không hợp lệ !!!");
                System.out.println(" Email phải có dạng: abc.@gmail.com...");
            } else {
                email = inputEmail;
                break;
            }
        }
        return email;
    }

    public void writeFile(ArrayList<Contact> contactList, String path) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
            for (Contact contact : contactList) {
                bufferedWriter.write(contact.getPhoneNumber() + "," + contact.getGroup() + "," + contact.getName() + "," + contact.getGender() + ","
                + contact.getAddress() + "," + contact.getDateOfBirth() + "," + contact.getEmail() + "\n");
            }
            bufferedWriter.close();
            System.out.println("Write file successfully !");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public ArrayList<Contact> readFile(String path) {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] strings = line.split(",");
                contacts.add(new Contact(strings[0], strings[1], strings[2], strings[3], strings[4], LocalDate.parse(strings[5], DateTimeFormatter.ISO_LOCAL_DATE), strings[6]));
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return contacts;
    }
}
