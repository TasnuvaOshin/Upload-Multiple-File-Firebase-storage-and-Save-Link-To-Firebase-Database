

		public class MainActivity extends AppCompatActivity {

			private static final int PICK_IMAGE = 1;
			Button upload,choose;
			TextView alert;

			ArrayList<Uri> FileList = new ArrayList<Uri>();
			private Uri FileUri;
			private ProgressDialog progressDialog;
			private int upload_count = 0;


			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
				alert = findViewById(R.id.alert);
				upload = findViewById(R.id.upload_image);
				choose = findViewById(R.id.chooser);
				progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("File Uploading Please Wait...........");


				choose.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {


						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						 intent.setType("*/*");
						intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
						startActivityForResult(intent,PICK_IMAGE);

					}
				});



				upload.setOnClickListener(new View.OnClickListener() {
					@SuppressLint("SetTextI18n")
					@Override
					public void onClick(View view) {

						progressDialog.show();
						 alert.setText("If Loading Takes too long please Press the button again");

				   StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("FileFolder");


				   for(upload_count = 0; upload_count < FileList.size(); upload_count++){


					   Uri IndividualFile = FileList.get(upload_count);
					   final StorageReference ImageName = ImageFolder.child("Image"+IndividualFile.getLastPathSegment());



					  ImageName.putFile(IndividualFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
						  @Override
						  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

							  ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
								  @Override
								  public void onSuccess(Uri uri) {
									 String url = String.valueOf(uri);
									 StoreLink(url);

								  }
							  });







						  }
					  });



				   }





					}
				});




			}

			private void StoreLink(String url) {


				DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserOne");

				HashMap<String,String> hashMap = new HashMap<>();
				hashMap.put("Filelink",url);


				databaseReference.push().setValue(hashMap);

				progressDialog.dismiss();
				alert.setText("File Uploaded Successfully");
				upload.setVisibility(View.GONE);
				FileList.clear();






			}


			@SuppressLint("SetTextI18n")
			@Override
			protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
				super.onActivityResult(requestCode, resultCode, data);

				if(requestCode == PICK_IMAGE){

					if(resultCode == RESULT_OK){

						if(data.getClipData() != null){



							int countClipData = data.getClipData().getItemCount();



							int currentImageSelect = 0;

							while (currentImageSelect < countClipData){


							   FileUri = data.getClipData().getItemAt(currentImageSelect).getUri();

								FileList.add(FileUri);

							   currentImageSelect = currentImageSelect +1;


							}

							alert.setVisibility(View.VISIBLE);
							alert.setText("You Have Selected "+ FileList.size() +" Images");
							choose.setVisibility(View.GONE);


						}else{


							Toast.makeText(this, "Please Select Multiple File", Toast.LENGTH_SHORT).show();
						}


					}


				}

			}
		}





